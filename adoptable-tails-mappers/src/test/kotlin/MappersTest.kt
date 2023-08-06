package ru.otus.otuskotlin.adoptabletails.mappers

import org.junit.Test
import ru.otus.otuskotlin.adoptabletails.common.PetAdContext
import ru.otus.otuskotlin.adoptabletails.common.models.PetAdCommand
import ru.otus.otuskotlin.adoptabletails.common.models.PetAdError
import ru.otus.otuskotlin.adoptabletails.common.models.PetAdRequestId
import ru.otus.otuskotlin.adoptabletails.common.models.PetAdState
import ru.otus.otuskotlin.adoptabletails.common.models.PetAdWorkMode
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAd
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetTemperament
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetType
import ru.otus.otuskotlin.adoptabletails.common.stubs.PetAdStubs
import ru.otus.otuskotlin.adoptabletails.mappers.mapper.fromTransportPetAd
import ru.otus.otuskotlin.adoptabletails.mappers.mapper.toTransportPetAd
import ru.otus.otuskotlin.api.models.PetAdCreateObject
import ru.otus.otuskotlin.api.models.PetAdCreateRequest
import ru.otus.otuskotlin.api.models.PetAdCreateResponse
import ru.otus.otuskotlin.api.models.PetAdDebug
import ru.otus.otuskotlin.api.models.PetAdRequestDebugMode
import ru.otus.otuskotlin.api.models.PetAdRequestDebugStubs
import java.math.BigDecimal
import kotlin.test.assertEquals

class MappersTest {

    @Test
    fun fromTransportPetAd() {
        val req = PetAdCreateRequest(
            requestType = "create",
            requestId = "1234",
            debug = PetAdDebug(
                mode = PetAdRequestDebugMode.STUB,
                stub = PetAdRequestDebugStubs.SUCCESS,
            ),
            petAd = PetAdCreateObject(
                name = "test name",
                description = "test description",
                age = BigDecimal.valueOf(3),
                breed = "test breed",
                petType = "DOG",
                temperament = "PHLEGMATIC",
                propertySize = "medium"
            ),
        )

        val context = PetAdContext().fromTransportPetAd(req)

        assertEquals(PetAdStubs.SUCCESS, context.stub)
        assertEquals(PetAdWorkMode.STUB, context.workMode)
        assertEquals(PetAdCommand.CREATE, context.command)
        assertEquals("test name", context.petAdRequest.name)
        assertEquals("test description", context.petAdRequest.description)
        assertEquals(BigDecimal.valueOf(3), context.petAdRequest.age)
        assertEquals("test breed", context.petAdRequest.breed)
        assertEquals(PetType.DOG, context.petAdRequest.petType)
        assertEquals(PetTemperament.PHLEGMATIC, context.petAdRequest.temperament)
        assertEquals("medium", context.petAdRequest.size)
    }

    @Test
    fun toTransportOrder() {
        val context = PetAdContext(
            requestId = PetAdRequestId("1234"),
            command = PetAdCommand.CREATE,
            petAdResponse = PetAd(
                name = "test name",
                description = "test description",
                age = BigDecimal.ONE,
                breed = "test breed",
                petType = PetType.DOG,
                temperament = PetTemperament.MELANCHOLIC,
                size = "big",
            ),
            errors = listOf(
                PetAdError(
                    code = "err",
                    group = "request",
                    field = "title",
                    message = "wrong title",
                    exception = Exception("test exception")
                )
            ).toMutableList(),
            state = PetAdState.RUNNING,
        )

        val req = context.toTransportPetAd() as PetAdCreateResponse

        assertEquals("1234", req.requestId)
        assertEquals("test name", req.petAd?.name)
        assertEquals("test description", req.petAd?.description)
        assertEquals(BigDecimal.ONE, req.petAd?.age)
        assertEquals("test breed", req.petAd?.breed)
        assertEquals("DOG", req.petAd?.petType)
        assertEquals("MELANCHOLIC", req.petAd?.temperament)
        assertEquals("big", req.petAd?.propertySize)



        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("wrong title", req.errors?.firstOrNull()?.message)
    }
}