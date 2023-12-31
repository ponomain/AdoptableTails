package ru.otus.otuskotlin.adoptabletails.biz.repository

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
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAdStatus
import ru.otus.otuskotlin.adoptabletails.common.permissions.CommonPrincipalModel
import ru.otus.otuskotlin.adoptabletails.common.permissions.CommonUserGroups
import ru.otus.otuskotlin.adoptabletails.common.repository.DbAdResponse
import ru.otus.otuskotlin.adoptabletails.repository.tests.AdRepositoryMock
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class AdRepositoryUpdateTest {
    private val command = AdoptableTailsCommand.UPDATE
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
    fun repositoryCreateSuccessTest() = runTest {
        val context = AdoptableTailsContext(
            command = command,
            state = AdoptableTailsState.NONE,
            workMode = AdoptableTailsWorkMode.TEST,
            petAdRequest = PetAd(
                id = PetAdId(adId),
                petAdStatus = PetAdStatus.ADOPTED,
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
        assertEquals(PetAdStatus.ADOPTED, context.petAdResponse.petAdStatus)
        assertEquals(Instant.parse("2023-03-03T08:05:57Z"), context.petAdResponse.createdAt)
    }
}