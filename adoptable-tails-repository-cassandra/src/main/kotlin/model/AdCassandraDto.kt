package ru.otus.otuskotlin.adoptabletails.repository.cassandra.model

import com.datastax.oss.driver.api.core.type.DataTypes
import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.datastax.oss.driver.api.mapper.annotations.Entity
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import kotlinx.datetime.Instant
import ru.otus.otuskotlin.adoptabletails.common.NONE
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAd
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAdId
import java.math.BigDecimal

@Entity
data class AdCassandraDto(
    @CqlName(COLUMN_ID)
    @PartitionKey
    var id: String? = null,

    @CqlName(COLUMN_NAME)
    var name: String? = null,

    @CqlName(COLUMN_BREED)
    var breed: String? = null,

    @CqlName(COLUMN_TYPE)
    var type: PetTypeCassandra? = null,

    @CqlName(COLUMN_AGE)
    var age: BigDecimal? = null,

    @CqlName(COLUMN_TEMPERAMENT)
    var temperament: PetTemperamentCassandra? = null,

    @CqlName(COLUMN_SIZE)
    var size: String? = null,

    @CqlName(COLUMN_DESCRIPTION)
    var description: String? = null,

    @CqlName(COLUMN_CREATED_AT)
    var createdAt: Instant? = null,

    @CqlName(COLUMN_UPDATED_AT)
    var updatedAt: Instant? = null,

    @CqlName(COLUMN_AD_STATUS)
    var adStatus: AdStatusCassandra? = null,
) {
    constructor(adModel: PetAd) : this(
        id = adModel.id.takeIf { it != PetAdId.NONE }?.asString(),
        name = adModel.name.takeIf { it.isNotEmpty() },
        breed = adModel.breed.takeIf { it.isNotEmpty() },
        type = adModel.petType.toTransport(),
        age = adModel.age.takeIf { it >= BigDecimal.ZERO },
        temperament = adModel.temperament.toTransport(),
        size = adModel.size.takeIf { it.isNotEmpty() },
        description = adModel.description.takeIf { it.isNotEmpty() },
        createdAt = adModel.createdAt,
        updatedAt = adModel.updatedAt,
        adStatus = adModel.petAdStatus.toTransport()
    )

    fun toAdModel(): PetAd = PetAd(
        id = id?.let { PetAdId(it) } ?: PetAdId.NONE,
        name = name?.let { it } ?: "",
        breed = breed?.let { it } ?: "",
        petType = type.fromTransport(),
        age = age?.let { it } ?: BigDecimal.ZERO,
        temperament = temperament.fromTransport(),
        size = size?.let { it } ?: "",
        description = description?.let { it } ?: "",
        createdAt = createdAt?.let { it } ?: Instant.NONE,
        updatedAt = updatedAt?.let { it } ?: Instant.NONE,
        petAdStatus = adStatus.fromTransport()
    )

    companion object {
        const val TABLE_NAME = "ads"

        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_BREED = "breed"
        const val COLUMN_TYPE = "type"
        const val COLUMN_AGE = "age"
        const val COLUMN_TEMPERAMENT = "temperament"
        const val COLUMN_SIZE = "size"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_CREATED_AT = "created_at"
        const val COLUMN_UPDATED_AT = "updated_at"
        const val COLUMN_AD_STATUS = "ad_status"

        fun table(keyspace: String, tableName: String) =
            SchemaBuilder
                .createTable(keyspace, tableName)
                .ifNotExists()
                .withPartitionKey(COLUMN_ID, DataTypes.TEXT)
                .withColumn(COLUMN_NAME, DataTypes.TEXT)
                .withColumn(COLUMN_BREED, DataTypes.TEXT)
                .withColumn(COLUMN_TYPE, DataTypes.TEXT)
                .withColumn(COLUMN_AGE, DataTypes.DECIMAL)
                .withColumn(COLUMN_TEMPERAMENT, DataTypes.TEXT)
                .withColumn(COLUMN_SIZE, DataTypes.TEXT)
                .withColumn(COLUMN_DESCRIPTION, DataTypes.TEXT)
                .withColumn(COLUMN_CREATED_AT, DataTypes.TIMESTAMP)
                .withColumn(COLUMN_UPDATED_AT, DataTypes.TIMESTAMP)
                .withColumn(COLUMN_AD_STATUS, DataTypes.TEXT)
                .build()
    }
}