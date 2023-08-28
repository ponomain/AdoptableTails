package ru.otus.otuskotlin.adoptabletails.repository.tests

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAd
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetTemperament
import ru.otus.otuskotlin.adoptabletails.common.repository.AdRepository
import ru.otus.otuskotlin.adoptabletails.common.repository.DbAdFilterRequest
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepositoryAdSearchTest {
    abstract val repository: AdRepository

    protected open val initializedObjects: List<PetAd> = initObjects

    @Test
    fun searchBreed() = runRepositoryTest {
        val result = repository.searchAd(DbAdFilterRequest(breed = "Unknown"))
        assertEquals(true, result.isSuccess)
        val expected = listOf(initializedObjects[2]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data?.sortedBy { it.id.asString() })
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun searchTemperament() = runRepositoryTest {
        val result = repository.searchAd(DbAdFilterRequest(temperament = PetTemperament.SANGUINE))
        assertEquals(true, result.isSuccess)
        val expected = listOf(initializedObjects[6]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data?.sortedBy { it.id.asString() })
        assertEquals(emptyList(), result.errors)
    }

    companion object : BaseInitAds("search") {
        override val initObjects: List<PetAd> = listOf(
            createInitTestModel("ad1"),
            createInitTestModel("ad2", breed = "Husky"),
            createInitTestModel("ad3", breed = "Unknown"),
            createInitTestModel("ad4", petType = "dog"),
            createInitTestModel("ad5", petType = "cat"),
            createInitTestModel("ad6", petType = "dog"),
            createInitTestModel("ad7", temperament = PetTemperament.SANGUINE),
            createInitTestModel("ad8", temperament = PetTemperament.CHOLERIC),
        )
    }
}