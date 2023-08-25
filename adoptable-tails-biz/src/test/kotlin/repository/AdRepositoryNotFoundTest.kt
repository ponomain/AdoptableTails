package ru.otus.otuskotlin.adoptabletails.biz.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import ru.otus.otuskotlin.adoptabletails.biz.AdoptableTailsProcessor
import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsContext
import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsCorSettings
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsCommand
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsError
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsState
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsWorkMode
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAd
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAdId
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetTemperament
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetType
import ru.otus.otuskotlin.adoptabletails.common.repository.DbAdResponse
import ru.otus.otuskotlin.adoptabletails.repository.tests.AdRepositoryMock
import java.math.BigDecimal
import kotlin.test.assertEquals

private val command = AdoptableTailsCommand.READ
private val adId = "10000000-0000-0000-0000-000000000001"
private val createdAt = Instant.parse("2023-03-03T08:05:57Z");
private val initOrder = PetAd(
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
private val repository = AdRepositoryMock(
    invokeReadAd = {
        if (it.id == initOrder.id) {
            DbAdResponse(
                isSuccess = true,
                data = initOrder
            )
        } else DbAdResponse(
            isSuccess = false,
            data = null,
            errors = listOf(AdoptableTailsError(message = "Not found", field = "id"))
        )
    }
)
private val settings = AdoptableTailsCorSettings(
    repositoryTest = repository
)
private val processor = AdoptableTailsProcessor(settings)

@OptIn(ExperimentalCoroutinesApi::class)
fun repositoryNotFoundTest(command: AdoptableTailsCommand) = runTest {
    val context = AdoptableTailsContext(
        command = command,
        state = AdoptableTailsState.NONE,
        workMode = AdoptableTailsWorkMode.TEST,
        petAdRequest = PetAd(
            id = PetAdId("12345"),
        )
    )

    processor.exec(context)

    assertEquals(AdoptableTailsState.FAILING, context.state)
    assertEquals(PetAd(), context.petAdResponse)
    assertEquals(1, context.errors.size)
    assertEquals("id", context.errors.first().field)
}