package ru.otus.otuskotlin.adoptabletails.repository.tests

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAd
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAdId
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAdStatus
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetTemperament
import ru.otus.otuskotlin.adoptabletails.common.repository.AdRepository
import ru.otus.otuskotlin.adoptabletails.common.repository.DbAdRequest
import java.math.BigDecimal
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepositoryAdCreateTest {
    abstract val repository: AdRepository

    private val createObject = PetAd(
        name = "Sparky",
        breed = "Hasky",
        age = BigDecimal(4.7),
        temperament = PetTemperament.SANGUINE,
        description = "Good doggy",
        size = "Above average",
        petAdStatus = PetAdStatus.CREATED,
        createdAt = Instant.parse("2023-03-03T08:05:57Z")
    )

    @Test
    fun createSuccess() = runRepositoryTest {

        val result = repository.createAd(DbAdRequest(createObject))

        val expected = createObject.copy(id = result.data?.id ?: PetAdId.NONE)
        assertEquals(true, result.isSuccess)
        assertEquals(expected.name, result.data?.name)
        assertEquals(expected.breed, result.data?.breed)
        assertEquals(expected.description, result.data?.description)
        assertEquals(expected.age, result.data?.age)
        assertEquals(expected.size, result.data?.size)
        assertEquals(expected.petAdStatus, result.data?.petAdStatus)
        assertNotEquals(PetAdId.NONE, result.data?.id)
        assertEquals(emptyList(), result.errors)
    }

    companion object : BaseInitAds("create") {
        override val initObjects: List<PetAd> = emptyList()
    }
}