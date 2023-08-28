package ru.otus.otuskotlin.adoptabletails.repository.tests

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAd
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAdStatus
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetTemperament
import ru.otus.otuskotlin.adoptabletails.common.repository.AdRepository
import ru.otus.otuskotlin.adoptabletails.common.repository.DbAdRequest
import ru.otus.otuskotlin.adoptabletails.common.repository.DbAdUpdateRequest
import java.math.BigDecimal
import java.time.Instant
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepositoryAdUpdateTest {
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
    fun updateSuccess() = runRepositoryTest {

        val id = repository.createAd(DbAdRequest(createObject)).data?.id
        val result = repository.updateAd(DbAdUpdateRequest(id!!, PetAdStatus.ADOPTED))
        assertEquals(true, result.isSuccess)
        assertEquals(PetAdStatus.ADOPTED, result.data?.petAdStatus)

    }

    companion object : BaseInitAds("create") {
        override val initObjects: List<PetAd> = emptyList()
    }
}