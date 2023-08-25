package ru.otus.otuskotlin.adoptabletails.biz.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import ru.otus.otuskotlin.adoptabletails.biz.AdoptableTailsProcessor
import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsContext
import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsCorSettings
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsCommand
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsState
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsWorkMode
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAd
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAdFilter
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAdId
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetTemperament
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetType
import ru.otus.otuskotlin.adoptabletails.common.repository.DbAdsResponse
import ru.otus.otuskotlin.adoptabletails.repository.tests.AdRepositoryMock
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class AdRepositorySearchTest {
    private val command = AdoptableTailsCommand.SEARCH
    private val adId = "10000000-0000-0000-0000-000000000001"
    private val createdAt = Instant.parse("2023-03-03T08:05:57Z");
    private val repository = AdRepositoryMock(
        invokeSearchAd = {
            DbAdsResponse(
                isSuccess = true,
                data = listOf(
                    PetAd(
                        id = PetAdId(adId),
                        name = "Fluffy",
                        breed = "Maine coon",
                        petType = PetType.CAT,
                        age = BigDecimal(3.8),
                        temperament = PetTemperament.CHOLERIC,
                        size = "Above average",
                        description = "Cute and kind cat",
                        createdAt = createdAt
                    )
                )
            )
        }
    )
    private val settings = AdoptableTailsCorSettings(
        repositoryTest = repository
    )
    private val processor = AdoptableTailsProcessor(settings)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun repositorySearchSuccessTest() = runTest {
        val context = AdoptableTailsContext(
            command = command,
            state = AdoptableTailsState.NONE,
            workMode = AdoptableTailsWorkMode.TEST,
            petAdFilter = PetAdFilter(
                type = PetType.CAT,
                temperament = PetTemperament.CHOLERIC,
                breed = "Maine coon"
            ),
        )

        processor.exec(context)

        assertEquals(AdoptableTailsState.FINISHING, context.state)
        assertNotEquals(PetAdId.NONE, context.petAdsResponse.first().id)
        assertEquals(BigDecimal(3.8), context.petAdsResponse.first().age)
        assertEquals("Fluffy", context.petAdsResponse.first().name)
        assertEquals("Maine coon", context.petAdsResponse.first().breed)
        assertEquals(PetType.CAT, context.petAdsResponse.first().petType)
        assertEquals(PetTemperament.CHOLERIC, context.petAdsResponse.first().temperament)
        assertEquals("Above average", context.petAdsResponse.first().size)
        assertEquals("Cute and king cat", context.petAdsResponse.first().description)
        assertEquals(Instant.parse("2023-03-03T08:05:57Z"), context.petAdsResponse.first().createdAt)
    }

}