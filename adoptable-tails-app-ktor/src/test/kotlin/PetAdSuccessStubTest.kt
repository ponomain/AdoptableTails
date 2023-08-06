package ru.otus.otuskotlin.adoptabletails.app.ktor

import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.spec.style.Test
import io.kotest.matchers.shouldBe
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import ru.otus.otuskotlin.adoptabletails.api.apiV1Mapper
import ru.otus.otuskotlin.adoptabletails.common.PetAdContext
import ru.otus.otuskotlin.adoptabletails.common.models.PetAdCommand
import ru.otus.otuskotlin.adoptabletails.common.models.PetAdRequestId
import ru.otus.otuskotlin.adoptabletails.mappers.mapper.toTransportPetAd
import ru.otus.otuskotlin.adoptabletails.stubs.PetAdStub
import ru.otus.otuskotlin.api.models.PetAdCreateObject
import ru.otus.otuskotlin.api.models.PetAdCreateRequest
import ru.otus.otuskotlin.api.models.PetAdCreateResponse
import ru.otus.otuskotlin.api.models.PetAdDebug
import ru.otus.otuskotlin.api.models.PetAdDeleteObject
import ru.otus.otuskotlin.api.models.PetAdDeleteRequest
import ru.otus.otuskotlin.api.models.PetAdDeleteResponse
import ru.otus.otuskotlin.api.models.PetAdGetObject
import ru.otus.otuskotlin.api.models.PetAdGetRequest
import ru.otus.otuskotlin.api.models.PetAdGetResponse
import ru.otus.otuskotlin.api.models.PetAdRequestDebugMode
import ru.otus.otuskotlin.api.models.PetAdRequestDebugStubs
import ru.otus.otuskotlin.api.models.PetAdSearchFilter
import ru.otus.otuskotlin.api.models.PetAdSearchRequest
import ru.otus.otuskotlin.api.models.PetAdSearchResponse
import ru.otus.otuskotlin.api.models.PetAdStatus
import ru.otus.otuskotlin.api.models.PetAdUpdateObject
import ru.otus.otuskotlin.api.models.PetAdUpdateRequest
import ru.otus.otuskotlin.api.models.PetAdUpdateResponse
import ru.otus.otuskotlin.api.models.PetTemperament
import ru.otus.otuskotlin.api.models.PetType
import java.math.BigDecimal

@Test
class PetAdSuccessStubTest : FunSpec({

    val createRequest = PetAdCreateRequest(
        requestType = "create",
        requestId = "123",
        debug = PetAdDebug(
            mode = PetAdRequestDebugMode.STUB,
            stub = PetAdRequestDebugStubs.SUCCESS
        ),
        petAd = PetAdCreateObject(
            name = "Waffle",
            description = "Very energetic and playfull dog",
            breed = "Corgi",
            petType = PetType.DOG.name,
            age = BigDecimal.valueOf(3.3),
            temperament = PetTemperament.MELANCHOLIC.name,
            propertySize = "below average"
        )
    )

    val createResponse = PetAdContext().apply {
        requestId = PetAdRequestId("123")
        petAdResponse = PetAdStub.getPetAd()
        command = PetAdCommand.CREATE
    }.toTransportPetAd()

    val updateRequest = PetAdUpdateRequest(
        requestType = "update",
        requestId = "123",
        debug = PetAdDebug(
            mode = PetAdRequestDebugMode.STUB,
            stub = PetAdRequestDebugStubs.SUCCESS
        ),
        petAd = PetAdUpdateObject(
            adStatus = PetAdStatus.ADOPTED.name
        )
    )

    val updateResponse = PetAdContext().apply {
        requestId = PetAdRequestId("123")
        petAdResponse = PetAdStub.getPetAd()
        command = PetAdCommand.UPDATE
    }.toTransportPetAd()

    val readRequest = PetAdGetRequest(
        requestType = "read",
        requestId = "123",
        debug = PetAdDebug(
            mode = PetAdRequestDebugMode.STUB,
            stub = PetAdRequestDebugStubs.SUCCESS
        ),
        petAd = PetAdGetObject(
            id = "123"
        )
    )

    val readResponse = PetAdContext().apply {
        requestId = PetAdRequestId("123")
        petAdResponse = PetAdStub.getPetAd()
        command = PetAdCommand.READ
    }.toTransportPetAd()


    val deleteRequest = PetAdDeleteRequest(
        requestType = "delete",
        requestId = "123",
        debug = PetAdDebug(
            mode = PetAdRequestDebugMode.STUB,
            stub = PetAdRequestDebugStubs.SUCCESS
        ),
        petAd = PetAdDeleteObject(
            id = "123"
        )
    )

    val deleteResponse = PetAdContext().apply {
        requestId = PetAdRequestId("123")
        command = PetAdCommand.DELETE
    }.toTransportPetAd()

    val searchRequest = PetAdSearchRequest(
        requestType = "search",
        requestId = "123",
        debug = PetAdDebug(
            mode = PetAdRequestDebugMode.STUB,
            stub = PetAdRequestDebugStubs.SUCCESS
        ),
        petAdFilter = PetAdSearchFilter("Corgi", PetType.DOG.name, PetTemperament.MELANCHOLIC.name)
    )

    val searchResponse = PetAdContext().apply {
        requestId = PetAdRequestId("123")
        petAdsResponse = PetAdStub.getPetAds()
        command = PetAdCommand.SEARCH
    }.toTransportPetAd()


    test("Create request success stub") {
        testApplication {
            val client = createClient {
                install(ContentNegotiation) {
                    jackson {
                        setConfig(apiV1Mapper.serializationConfig)
                        setConfig(apiV1Mapper.deserializationConfig)
                    }
                }
            }
            val response = client.post("/api/pet-ad/create") {
                contentType(ContentType.Application.Json)
                setBody(createRequest)
            }
            response shouldHaveStatus HttpStatusCode.OK
            response.body() as PetAdCreateResponse shouldBe createResponse
        }
    }

    test("update success stub") {
        testApplication {
            val client = createClient {
                install(ContentNegotiation) {
                    jackson {
                        setConfig(apiV1Mapper.serializationConfig)
                        setConfig(apiV1Mapper.deserializationConfig)
                    }
                }
            }
            val response = client.post("/api/pet-ad/update") {
                contentType(ContentType.Application.Json)
                setBody(updateRequest)
            }
            response shouldHaveStatus HttpStatusCode.OK
            response.body() as PetAdUpdateResponse shouldBe updateResponse
        }
    }

    test("read success stub") {
        testApplication {
            val client = createClient {
                install(ContentNegotiation) {
                    jackson {
                        setConfig(apiV1Mapper.serializationConfig)
                        setConfig(apiV1Mapper.deserializationConfig)
                    }
                }
            }
            val response = client.post("/api/pet-ad/read") {
                contentType(ContentType.Application.Json)
                setBody(readRequest)
            }
            response shouldHaveStatus HttpStatusCode.OK
            response.body() as PetAdGetResponse shouldBe readResponse
        }
    }

    test("delete success stub") {
        testApplication {
            val client = createClient {
                install(ContentNegotiation) {
                    jackson {
                        setConfig(apiV1Mapper.serializationConfig)
                        setConfig(apiV1Mapper.deserializationConfig)
                    }
                }
            }
            val response = client.post("/api/pet-ad/delete") {
                contentType(ContentType.Application.Json)
                setBody(deleteRequest)
            }
            response shouldHaveStatus HttpStatusCode.OK
            response.body() as PetAdDeleteResponse shouldBe deleteResponse
        }
    }

    test("search success stub") {
        testApplication {
            val client = createClient {
                install(ContentNegotiation) {
                    jackson {
                        setConfig(apiV1Mapper.serializationConfig)
                        setConfig(apiV1Mapper.deserializationConfig)
                    }
                }
            }
            val response = client.post("/api/pet-ad/search") {
                contentType(ContentType.Application.Json)
                setBody(searchRequest)
            }
            response shouldHaveStatus HttpStatusCode.OK
            response.body() as PetAdSearchResponse shouldBe searchResponse
        }
    }

})