package ru.otus.otuskotlin.adoptabletails.repository.tests

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAd
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAdId
import ru.otus.otuskotlin.adoptabletails.common.repository.AdRepository
import ru.otus.otuskotlin.adoptabletails.common.repository.DbAdIdRequest
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepositoryAdDeleteTest {
    abstract val repository: AdRepository

    protected open val deleteSuccess = initObjects[0]
    private val notFoundId = PetAdId("ad-repository-delete-notFound")

    @Test
    fun deleteSuccess() = runRepositoryTest {
        val result = repository.deleteAd(DbAdIdRequest(deleteSuccess.id))

        assertEquals(true, result.isSuccess)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun deleteNotFound() = runRepositoryTest {
        val result = repository.readAd(DbAdIdRequest(notFoundId))

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitAds("delete") {
        override val initObjects: List<PetAd> = listOf(
            createInitTestModel("delete")
        )
    }
}