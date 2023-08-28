package ru.otus.otuskotlin.adoptabletails.api

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.otus.otuskotlin.api.models.IRequest
import ru.otus.otuskotlin.api.models.PetAdCreateObject
import ru.otus.otuskotlin.api.models.PetAdCreateRequest
import ru.otus.otuskotlin.api.models.PetAdDebug
import ru.otus.otuskotlin.api.models.PetAdRequestDebugMode
import ru.otus.otuskotlin.api.models.PetAdRequestDebugStubs
import ru.otus.otuskotlin.api.models.PetTemperament
import ru.otus.otuskotlin.api.models.PetType
import java.math.BigDecimal
import kotlin.test.assertContains

class RequestSerializationTest {
    private val request = PetAdCreateRequest(
        requestId = "requestId123id",
        debug = PetAdDebug(
            mode = PetAdRequestDebugMode.STUB,
            stub = PetAdRequestDebugStubs.CANNOT_READ
        ),
        petAd = PetAdCreateObject(
            name = "Sparkle",
            breed = "Unknown",
            age = BigDecimal.ONE,
            temperament = PetTemperament.CHOLERIC.name,
            propertySize = "Small",
            description = "test"
        )
    )

    @Test
    fun serialize() {

        val json = apiV1Mapper.writeValueAsString(request)
        assertContains(json, Regex("\"requestId\":\\s*\"requestId123id\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"cannotRead\""))
        assertContains(json, Regex("\"name\":\\s*\"Sparkle\""))
        assertContains(json, Regex("\"breed\":\\s*\"Unknown\""))
        assertContains(json, Regex("\"age\":\\d*1"))
        assertContains(json, Regex("\"temperament\":\\s*\"CHOLERIC\""))
        assertContains(json, Regex("\"size\":\\s*\"Small\""))
        assertContains(json, Regex("\"description\":\\s*\"test\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(request)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as PetAdCreateRequest
        assertEquals(request, obj)
    }
}