package ru.otus.otuskotlin.adoptabletails.repository.cassandra

import com.datastax.oss.driver.api.core.cql.AsyncResultSet
import com.datastax.oss.driver.api.mapper.MapperContext
import com.datastax.oss.driver.api.mapper.entity.EntityHelper
import com.datastax.oss.driver.api.querybuilder.QueryBuilder
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetTemperament
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetType
import ru.otus.otuskotlin.adoptabletails.common.repository.DbAdFilterRequest
import ru.otus.otuskotlin.adoptabletails.repository.cassandra.model.AdCassandraDto
import ru.otus.otuskotlin.adoptabletails.repository.cassandra.model.toTransport
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import java.util.function.BiConsumer

class AdCassandraSearch(
    private val context: MapperContext,
    private val entityHelper: EntityHelper<AdCassandraDto>
) {
    fun search(filter: DbAdFilterRequest): CompletionStage<Collection<AdCassandraDto>> {
        var select = entityHelper.selectStart().allowFiltering()

        if (filter.breed!= "") {
            select = select
                .whereColumn(AdCassandraDto.COLUMN_BREED)
                .isEqualTo(QueryBuilder.literal(filter.breed, context.session.context.codecRegistry))
        }
        if (filter.type != PetType.NONE) {
            select = select
                .whereColumn(AdCassandraDto.COLUMN_TYPE)
                .isEqualTo(QueryBuilder.literal(filter.type.toTransport(), context.session.context.codecRegistry))
        }
        if (filter.temperament != PetTemperament.NONE) {
            select = select
                .whereColumn(AdCassandraDto.COLUMN_TEMPERAMENT)
                .isEqualTo(QueryBuilder.literal(filter.temperament.toTransport(), context.session.context.codecRegistry))
        }

        val asyncFetcher = AsyncFetcher()

        context.session
            .executeAsync(select.build())
            .whenComplete(asyncFetcher)

        return asyncFetcher.stage
    }

    inner class AsyncFetcher : BiConsumer<AsyncResultSet?, Throwable?> {
        private val buffer = mutableListOf<AdCassandraDto>()
        private val future = CompletableFuture<Collection<AdCassandraDto>>()
        val stage: CompletionStage<Collection<AdCassandraDto>> = future

        override fun accept(resultSet: AsyncResultSet?, t: Throwable?) {
            when {
                t != null -> future.completeExceptionally(t)
                resultSet == null -> future.completeExceptionally(IllegalStateException("ResultSet should not be null"))
                else -> {
                    buffer.addAll(resultSet.currentPage().map { entityHelper.get(it, false) })
                    if (resultSet.hasMorePages())
                        resultSet.fetchNextPage().whenComplete(this)
                    else
                        future.complete(buffer)
                }
            }
        }
    }
}