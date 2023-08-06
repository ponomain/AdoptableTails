package ru.otus.otuskotlin.adoptabletails.api

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.otus.otuskotlin.api.models.IResponse
import ru.otus.otuskotlin.api.models.PetAdCreateResponse
import ru.otus.otuskotlin.api.models.PetAdResponseFullObject
import ru.otus.otuskotlin.api.models.PetAdStatus
import ru.otus.otuskotlin.api.models.PetTemperament
import ru.otus.otuskotlin.api.models.PetType
import ru.otus.otuskotlin.api.models.ResponseResult
import java.math.BigDecimal
import kotlin.test.assertContains

class ResponseSerializationTest {
    private val response = PetAdCreateResponse(
        requestId = "requestIdId123id",
        result = ResponseResult.SUCCESS,
        petAd = PetAdResponseFullObject(
            name = "Mike",
            description = "Good and kind cat",
            breed = "maine-coon",
            petType = PetType.CAT.name,
            age = BigDecimal.ONE,
            temperament = PetTemperament.SANGUINE.name,
            propertySize = "big",
            adStatus = PetAdStatus.RESERVED.name,
            id = "1234"
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(response)
        assertContains(json, Regex("\"requestId\":\\s*\"requestIdId123id\""))
        assertContains(json, Regex("\"result\":\\s*\"success\""))
        assertContains(json, Regex("\"name\":\\s*\"Mike\""))
        assertContains(json, Regex("\"description\":\\s*\"Good and kind cat\""))
        assertContains(json, Regex("\"breed\":\\s*\"maine-coon\""))
        assertContains(json, Regex("\"petType\":\\s*\"CAT\""))
        assertContains(json, Regex("\"age\":\\d*1"))
        assertContains(json, Regex("\"temperament\":\\s*\"SANGUINE\""))
        assertContains(json, Regex("\"size\":\\s*\"big\""))
        assertContains(json, Regex("\"adStatus\":\\W*\"RESERVED\""))
        assertContains(json, Regex("\"id\":\\W*\"1234\""))

    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(response)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as PetAdCreateResponse
        assertEquals(response, obj)
    }

}