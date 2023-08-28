package ru.otus.otuskotlin.adoptabletails.repository.cassandra

import com.datastax.oss.driver.api.mapper.annotations.Dao
import com.datastax.oss.driver.api.mapper.annotations.Delete
import com.datastax.oss.driver.api.mapper.annotations.Insert
import com.datastax.oss.driver.api.mapper.annotations.QueryProvider
import com.datastax.oss.driver.api.mapper.annotations.Select
import com.datastax.oss.driver.api.mapper.annotations.Update
import ru.otus.otuskotlin.adoptabletails.common.repository.DbAdFilterRequest
import ru.otus.otuskotlin.adoptabletails.repository.cassandra.model.AdCassandraDto
import java.util.concurrent.CompletionStage

@Dao
interface AdCassandraDao {

    @Insert
    fun create(adDto: AdCassandraDto): CompletionStage<Unit>

    @Update
    fun update(dto: AdCassandraDto): CompletionStage<Boolean>

    @Select
    fun read(id: String): CompletionStage<AdCassandraDto?>

    @Delete(customWhereClause = "id = :id", entityClass = [AdCassandraDto::class])
    fun delete(id: String): CompletionStage<Boolean>

    @QueryProvider(providerClass = AdCassandraSearch::class, entityHelpers = [AdCassandraDto::class])
    fun search(filter: DbAdFilterRequest): CompletionStage<Collection<AdCassandraDto>>
}