package ru.otus.otuskotlin.adoptabletails.repository.cassandra

import org.testcontainers.containers.CassandraContainer
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAd
import ru.otus.otuskotlin.adoptabletails.common.repository.AdRepository
import ru.otus.otuskotlin.adoptabletails.repository.tests.RepositoryAdCreateTest
import ru.otus.otuskotlin.adoptabletails.repository.tests.RepositoryAdDeleteTest
import ru.otus.otuskotlin.adoptabletails.repository.tests.RepositoryAdReadTest
import ru.otus.otuskotlin.adoptabletails.repository.tests.RepositoryAdSearchTest
import java.time.Duration
import java.util.*

class RepositoryOrderCassandraCreateTest : RepositoryAdCreateTest() {
    override val repository: AdRepository = TestCompanion.repository(initObjects, "ks_create")
}

class RepositoryOrderCassandraDeleteTest : RepositoryAdDeleteTest() {
    override val repository: AdRepository = TestCompanion.repository(initObjects, "delete")
}

class RepositoryOrderCassandraReadTest : RepositoryAdReadTest() {
    override val repository: AdRepository = TestCompanion.repository(initObjects, "read")
}

class RepositoryOrderCassandraSearchTest : RepositoryAdSearchTest() {
    override val repository: AdRepository = TestCompanion.repository(initObjects, "search")
}

class TestCasandraContainer : CassandraContainer<TestCasandraContainer>("cassandra:4.1.2")

object TestCompanion {
    private val container by lazy {
        TestCasandraContainer().withStartupTimeout(Duration.ofSeconds(300L))
            .also { it.start() }
    }

    fun repository(initObjects: List<PetAd>, keyspace: String): AdRepositoryCassandra {
        return AdRepositoryCassandra(
            keyspaceName = keyspace,
            host = container.host,
            port = container.getMappedPort(CassandraContainer.CQL_PORT),
            testing = true,
            randomUuid = { UUID.randomUUID().toString() },
            initObjects = initObjects
        )
    }
}