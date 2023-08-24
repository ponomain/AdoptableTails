package ru.otus.otuskotlin.adoptabletails.repository.tests

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAd
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAdId
import ru.otus.otuskotlin.adoptabletails.common.repository.AdRepository
import ru.otus.otuskotlin.adoptabletails.common.repository.DbAdIdRequest
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepositoryAdReadTest {
    abstract val repository: AdRepository
    protected open val readSuccess = initObjects[0]

    @Test
    fun readSuccess() = runRepositoryTest {
        val result = repository.readAd(DbAdIdRequest(readSuccess.id))

        assertEquals(true, result.isSuccess)
        assertEquals(readSuccess, result.data)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun readNotFound() = runRepositoryTest {
        val result = repository.readAd(DbAdIdRequest(notFoundId))

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitAds("read") {
        override val initObjects: List<PetAd> = listOf(
            createInitTestModel("read")
        )
        val notFoundId = PetAdId("ad-repository-read-notFound")
    }
}