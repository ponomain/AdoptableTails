package ru.otus.otuskotlin.adoptabletails.app.ktor.configs

import io.ktor.server.config.*

data class CassandraConfig(
    val host: String = "localhost",
    val port: Int = 9042,
    val username: String = "cassandra",
    val password: String = "cassandra",
    val keyspace: String = "test_keyspace"
) {
    constructor(config: ApplicationConfig) : this(
        host = config.property("$PATH.host").getString(),
        port = config.property("$PATH.port").getString().toInt(),
        username = config.property("$PATH.username").getString(),
        password = config.property("$PATH.password").getString(),
        keyspace = config.property("$PATH.keyspace").getString()
    )

    companion object {
        const val PATH = "${ConfigPaths.repository}.cassandra"
    }
}