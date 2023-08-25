package ru.otus.otuskotlin.adoptabletails.repository.cassandra

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.mapper.annotations.DaoFactory
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace
import com.datastax.oss.driver.api.mapper.annotations.DaoTable
import com.datastax.oss.driver.api.mapper.annotations.Mapper

@Mapper
interface CassandraMapper {
    @DaoFactory
    fun adDao(@DaoKeyspace keyspace: String, @DaoTable tableName: String): AdCassandraDao

    companion object {
        fun builder(session: CqlSession) = CassandraMapperBuilder(session)
    }
}