package ru.otus.otuskotlin.adoptabletails.biz.authorization

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.adoptabletails.biz.AdoptableTailsProcessor
import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsContext
import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsCorSettings
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsCommand
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsState
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsWorkMode
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAd
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAdId
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetTemperament
import ru.otus.otuskotlin.adoptabletails.common.permissions.CommonPrincipalModel
import ru.otus.otuskotlin.adoptabletails.common.permissions.CommonUserGroups
import ru.otus.otuskotlin.adoptabletails.common.repository.DbAdResponse
import ru.otus.otuskotlin.adoptabletails.repository.tests.AdRepositoryMock
import java.math.BigDecimal
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
class AdAuthorizationTest {
    private val command = AdoptableTailsCommand.CREATE
    private val adId = "10000000-0000-0000-0000-000000000001"
    private val createdAt = Instant.parse("2023-03-03T08:05:57Z");
    private val repository = AdRepositoryMock(
        invokeCreateAd = {
            DbAdResponse(
                isSuccess = true,
                data = PetAd(
                    id = PetAdId(adId),
                    name = it.ad.name,
                    breed = it.ad.breed,
                    age = it.ad.age,
                    temperament = it.ad.temperament,
                    size = it.ad.size,
                    description = it.ad.description,
                    createdAt = createdAt,
                    petAdStatus = it.ad.petAdStatus
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
    fun createSuccessTest() = runTest {
        val context = AdoptableTailsContext(
            command = command,
            state = AdoptableTailsState.NONE,
            workMode = AdoptableTailsWorkMode.TEST,
            petAdRequest = PetAd(
                name = "Fluffy",
                breed = "Maine coon",
                age = BigDecimal(3.8),
                temperament = PetTemperament.CHOLERIC,
                size = "Above average",
                description = "Cute and kind cat",
                createdAt = createdAt
            ),
            principal = CommonPrincipalModel(
                id = PetAdId("1011"),
                groups = setOf(
                    CommonUserGroups.USER,
                    CommonUserGroups.TEST,
                )
            ),
        )

        processor.exec(context)

        assertEquals(AdoptableTailsState.FINISHING, context.state)
        assertNotEquals(PetAdId.NONE, context.petAdResponse.id)
        assertEquals(BigDecimal(3.8), context.petAdResponse.age)
        assertEquals("Fluffy", context.petAdResponse.name)
        assertEquals("Maine coon", context.petAdResponse.breed)
        assertEquals(PetTemperament.CHOLERIC, context.petAdResponse.temperament)
        assertEquals("Above average", context.petAdResponse.size)
        assertEquals("Cute and king cat", context.petAdResponse.description)
        assertEquals(Instant.parse("2023-03-03T08:05:57Z"), context.petAdResponse.createdAt)
    }

}