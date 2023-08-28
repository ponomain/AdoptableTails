package ru.otus.otuskotlin.adoptabletails.repository.cassandra

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import com.datastax.oss.driver.internal.core.type.codec.extras.enums.EnumNameCodec
import com.datastax.oss.driver.internal.core.type.codec.registry.DefaultCodecRegistry
import kotlinx.coroutines.future.await
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.slf4j.LoggerFactory
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsError
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAd
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAdId
import ru.otus.otuskotlin.adoptabletails.common.models.helpers.asAdoptableTailsError
import ru.otus.otuskotlin.adoptabletails.common.repository.AdRepository
import ru.otus.otuskotlin.adoptabletails.common.repository.DbAdFilterRequest
import ru.otus.otuskotlin.adoptabletails.common.repository.DbAdIdRequest
import ru.otus.otuskotlin.adoptabletails.common.repository.DbAdRequest
import ru.otus.otuskotlin.adoptabletails.common.repository.DbAdResponse
import ru.otus.otuskotlin.adoptabletails.common.repository.DbAdUpdateRequest
import ru.otus.otuskotlin.adoptabletails.common.repository.DbAdsResponse
import ru.otus.otuskotlin.adoptabletails.repository.cassandra.model.AdCassandraDto
import ru.otus.otuskotlin.adoptabletails.repository.cassandra.model.AdStatusCassandra
import ru.otus.otuskotlin.adoptabletails.repository.cassandra.model.PetTemperamentCassandra
import ru.otus.otuskotlin.adoptabletails.repository.cassandra.model.toTransport
import java.net.InetAddress
import java.net.InetSocketAddress
import java.time.Instant
import java.util.*
import java.util.concurrent.CompletionStage
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class AdRepositoryCassandra(
    private val keyspaceName: String,
    private val host: String = "",
    private val port: Int = 9042,
    private val username: String = "cassandra",
    private val password: String = "cassandra",
    private val testing: Boolean = true,
    private val timeout: Duration = 30.toDuration(DurationUnit.SECONDS),
    private val randomUuid: () -> String = { UUID.randomUUID().toString() },
    initObjects: Collection<PetAd> = emptyList(),
) : AdRepository {
    private val log = LoggerFactory.getLogger(javaClass)

    private val codecRegistry by lazy {
        DefaultCodecRegistry("default").apply {
            register(EnumNameCodec(PetTemperamentCassandra::class.java))
            register(EnumNameCodec(AdStatusCassandra::class.java))
            register(EnumNameCodec(PetTemperamentCassandra::class.java))
        }
    }

    private val session by lazy {
        CqlSession.builder()
            .addContactPoints(parseAddresses(host, port))
            .withLocalDatacenter("datacenter1")
            .withAuthCredentials(username, password)
            .withCodecRegistry(codecRegistry)
            .build()
    }

    private val mapper by lazy { CassandraMapper.builder(session).build() }

    private fun createSchema(keyspace: String) {
        session.execute(
            SchemaBuilder
                .createKeyspace(keyspace)
                .ifNotExists()
                .withSimpleStrategy(1)
                .build()
        )
        session.execute(AdCassandraDto.table(keyspace, AdCassandraDto.TABLE_NAME))
    }

    private val dao by lazy {
        if (testing) {
            createSchema(keyspaceName)
        }
        mapper.adDao(keyspaceName, AdCassandraDto.TABLE_NAME).apply {
            runBlocking {
                initObjects.map { model ->
                    withTimeout(timeout) {
                        create(AdCassandraDto(model)).await()
                    }
                }
            }
        }
    }

    private fun errorToAdResponse(e: Exception) = DbAdResponse.error(e.asAdoptableTailsError())
    private fun errorToAdsResponse(e: Exception) = DbAdsResponse.error(e.asAdoptableTailsError())

    private suspend inline fun <DbRes, Response> doDbAction(
        name: String,
        crossinline daoAction: () -> CompletionStage<DbRes>,
        okToResponse: (DbRes) -> Response,
        errorToResponse: (Exception) -> Response
    ): Response = doDbAction(
        name,
        {
            val dbRes = withTimeout(timeout) { daoAction().await() }
            okToResponse(dbRes)
        },
        errorToResponse
    )

    private suspend inline fun readAndDoDbAction(
        name: String,
        id: PetAdId,
        successResult: PetAd?,
        daoAction: () -> CompletionStage<Boolean>,
        errorToResponse: (Exception) -> DbAdResponse
    ): DbAdResponse =
        if (id == PetAdId.NONE)
            ID_IS_EMPTY
        else doDbAction(
            name,
            {
                val read = dao.read(id.asString()).await()
                if (read == null) ID_NOT_FOUND
                else {
                    val success = daoAction().await()
                    if (success) DbAdResponse.success(successResult ?: read.toAdModel())
                    else DbAdResponse(
                        read.toAdModel(),
                        false
                    )
                }
            },
            errorToResponse
        )

    private inline fun <Response> doDbAction(
        name: String,
        daoAction: () -> Response,
        errorToResponse: (Exception) -> Response
    ): Response =
        try {
            daoAction()
        } catch (e: Exception) {
            log.error("Failed to $name", e)
            errorToResponse(e)
        }

    override suspend fun createAd(rq: DbAdRequest): DbAdResponse {
        val new = rq.ad.copy(id = PetAdId(randomUuid()), createdAt = Instant.now())
        return doDbAction(
            "create",
            { dao.create(AdCassandraDto(new)) },
            { DbAdResponse.success(new) },
            ::errorToAdResponse
        )
    }

    override suspend fun updateAd(rq: DbAdUpdateRequest): DbAdResponse {
        val existing = dao.read(rq.id.asString())
        val dto = existing.await()
        dto?.adStatus = rq.status.toTransport()
        dto?.updatedAt = Instant.now()
        return doDbAction(
            "update",
            { dao.update(dto!!) },
            { DbAdResponse.success(dto?.toAdModel()!!) },
            ::errorToAdResponse
        )
    }

    override suspend fun readAd(rq: DbAdIdRequest): DbAdResponse =
        if (rq.id == PetAdId.NONE)
            ID_IS_EMPTY
        else doDbAction(
            "read",
            { dao.read(rq.id.asString()) },
            { found ->
                if (found != null) DbAdResponse.success(found.toAdModel())
                else ID_NOT_FOUND
            },
            ::errorToAdResponse
        )

    override suspend fun deleteAd(rq: DbAdIdRequest): DbAdResponse =
        readAndDoDbAction(
            "delete",
            rq.id,
            null,
            { dao.delete(rq.id.asString()) },
            ::errorToAdResponse
        )

    override suspend fun searchAd(rq: DbAdFilterRequest): DbAdsResponse =
        doDbAction(
            "search",
            { dao.search(rq) },
            { found ->
                DbAdsResponse.success(found.map { it.toAdModel() })
            },
            ::errorToAdsResponse
        )

    companion object {
        private val ID_IS_EMPTY = DbAdResponse.error(AdoptableTailsError(field = "id", message = "Id is empty"))
        private val ID_NOT_FOUND =
            DbAdResponse.error(AdoptableTailsError(field = "id", code = "not-found", message = "Not Found"))
    }
}

private fun parseAddresses(hosts: String, port: Int): Collection<InetSocketAddress> = hosts
    .split(Regex("""\s*,\s*"""))
    .map { InetSocketAddress(InetAddress.getByName(it), port) }