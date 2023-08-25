package ru.otus.otuskotlin.adoptabletails.app.ktor.plugins

import io.ktor.server.application.*
import ru.otus.otuskotlin.adoptabletails.app.ktor.configs.AdDbType
import ru.otus.otuskotlin.adoptabletails.app.ktor.configs.CassandraConfig
import ru.otus.otuskotlin.adoptabletails.app.ktor.configs.ConfigPaths
import ru.otus.otuskotlin.adoptabletails.common.repository.AdRepository
import ru.otus.otuskotlin.adoptabletails.repository.cassandra.AdRepositoryCassandra

fun Application.getDatabaseConf(type: AdDbType): AdRepository {
    val dbSettingPath = "${ConfigPaths.repository}.${type.confName}"
    val dbSettings = environment.config.propertyOrNull(dbSettingPath)?.getString()?.lowercase()
    return when (dbSettings) {
        "cassandra" -> initCassandra()
        else -> throw IllegalArgumentException(
            "$dbSettingPath must be set in application.yaml to one of: " +
                    "'cassandra'"
        )
    }
}

private fun Application.initCassandra(): AdRepository {
    val config = CassandraConfig(environment.config)
    return AdRepositoryCassandra(
        keyspaceName = config.keyspace,
        host = config.host,
        port = config.port,
        username = config.username,
        password = config.password,
    )
}