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
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAdId
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetTemperament
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetType
import ru.otus.otuskotlin.adoptabletails.common.repository.DbAdResponse
import ru.otus.otuskotlin.adoptabletails.repository.tests.AdRepositoryMock
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class AdRepositoryReadTest {
    private val command = AdoptableTailsCommand.READ
    private val adId = "10000000-0000-0000-0000-000000000001"
    private val createdAt = Instant.parse("2023-03-03T08:05:57Z");
    private val repository = AdRepositoryMock(
        invokeReadAd = {
            DbAdResponse(
                isSuccess = true,
                data = PetAd(
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
        }
    )
    private val settings = AdoptableTailsCorSettings(
        repositoryTest = repository
    )
    private val processor = AdoptableTailsProcessor(settings)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun repositoryReadSuccessTest() = runTest {
        val context = AdoptableTailsContext(
            command = command,
            state = AdoptableTailsState.NONE,
            workMode = AdoptableTailsWorkMode.TEST,
            petAdRequest = PetAd(
                id = PetAdId(adId),
            ),
        )

        processor.exec(context)

        assertEquals(AdoptableTailsState.FINISHING, context.state)
        assertNotEquals(PetAdId.NONE, context.petAdResponse.id)
        assertEquals(BigDecimal(3.8), context.petAdResponse.age)
        assertEquals("Fluffy", context.petAdResponse.name)
        assertEquals("Maine coon", context.petAdResponse.breed)
        assertEquals(PetType.CAT, context.petAdResponse.petType)
        assertEquals(PetTemperament.CHOLERIC, context.petAdResponse.temperament)
        assertEquals("Above average", context.petAdResponse.size)
        assertEquals("Cute and king cat", context.petAdResponse.description)
        assertEquals(Instant.parse("2023-03-03T08:05:57Z"), context.petAdResponse.createdAt)
    }

    @Test
    fun repoReadNotFoundTest() = repositoryNotFoundTest(command)
}