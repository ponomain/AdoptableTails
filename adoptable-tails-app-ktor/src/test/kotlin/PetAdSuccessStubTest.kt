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
import ru.otus.otuskotlin.adoptabletails.app.ktor.authorization.addAuthorization
import ru.otus.otuskotlin.adoptabletails.app.ktor.configs.KtorAuthConfig
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAd
import ru.otus.otuskotlin.adoptabletails.common.permissions.CommonUserGroups
import ru.otus.otuskotlin.adoptabletails.common.repository.DbAdResponse
import ru.otus.otuskotlin.adoptabletails.common.repository.DbAdsResponse
import ru.otus.otuskotlin.adoptabletails.repository.tests.AdRepositoryMock
import ru.otus.otuskotlin.adoptabletails.stubs.PetAdStub
import ru.otus.otuskotlin.api.models.PetAdCreateObject
import ru.otus.otuskotlin.api.models.PetAdCreateRequest
import ru.otus.otuskotlin.api.models.PetAdCreateResponse
import ru.otus.otuskotlin.api.models.PetAdDebug
import ru.otus.otuskotlin.api.models.PetAdDeleteObject
import ru.otus.otuskotlin.api.models.PetAdDeleteRequest
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

    val stub = PetAdStub.getPetAd()
    val adId = stub.id

    test("Create request success stub") {
        testApplication {
            val repo = AdRepositoryMock(
                invokeCreateAd = {
                    DbAdResponse(
                        isSuccess = true,
                        data = it.ad.copy(id = adId),
                    )
                }
            )
            application { module(testSettings(repo)) }
            val client = createClient {
                install(ContentNegotiation) {
                    jackson {
                        setConfig(apiV1Mapper.serializationConfig)
                        setConfig(apiV1Mapper.deserializationConfig)
                    }
                }
            }
            val createRequest = PetAdCreateRequest(
                requestType = "create",
                requestId = "123",
                debug = PetAdDebug(
                    mode = PetAdRequestDebugMode.STUB,
                    stub = PetAdRequestDebugStubs.SUCCESS
                ),
                petAd = PetAdCreateObject(
                    name = "Sparky",
                    breed = "Unknown",
                    petType = ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetType.DOG.name,
                    age = BigDecimal.TEN,
                    temperament = PetTemperament.MELANCHOLIC.name,
                    propertySize = "BIG",
                    description = "Playfull and king dog"
                )
            )

            val response = client.post("/api/pet-ad/create") {
                contentType(ContentType.Application.Json)
                addAuthorization(id = "123", config = KtorAuthConfig.TEST, groups = listOf(CommonUserGroups.TEST.name))
                setBody(createRequest)
            }

            val petAdCreateResponse = response.body<PetAdCreateResponse>()
            response shouldHaveStatus HttpStatusCode.OK
            petAdCreateResponse.petAd?.id shouldBe "1234567890"
            petAdCreateResponse.petAd?.name shouldBe "Sparky"
            petAdCreateResponse.petAd?.description shouldBe "Playfull and king dog"
            petAdCreateResponse.petAd?.breed shouldBe "Unknown"
            petAdCreateResponse.petAd?.petType shouldBe "DOG"
            petAdCreateResponse.petAd?.age shouldBe "10"
            petAdCreateResponse.petAd?.temperament shouldBe "MELANCHOLIC"
            petAdCreateResponse.petAd?.propertySize shouldBe "BIG"
            petAdCreateResponse.petAd?.adStatus shouldBe "CREATED"
            petAdCreateResponse.requestId shouldBe "123"
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
            val updateRequest = PetAdUpdateRequest(
                requestType = "update",
                requestId = "12",
                debug = PetAdDebug(
                    mode = PetAdRequestDebugMode.STUB,
                    stub = PetAdRequestDebugStubs.SUCCESS
                ),
                petAd = PetAdUpdateObject(
                    id = "123",
                    adStatus = PetAdStatus.RESERVED.name
                )
            )

            val response = client.post("/api/pet-ad/update") {
                contentType(ContentType.Application.Json)
                addAuthorization(id = "123", config = KtorAuthConfig.TEST, groups = listOf(CommonUserGroups.TEST.name))
                setBody(updateRequest)
            }
            val petAdUpdateResponse = response.body<PetAdUpdateResponse>()
            response shouldHaveStatus HttpStatusCode.OK
            petAdUpdateResponse.petAd?.adStatus shouldBe "RESERVED"
        }
    }

    test("read success stub") {
        testApplication {
            val repo = AdRepositoryMock(
                invokeReadAd = {
                    DbAdResponse(
                        isSuccess = true,
                        data = PetAd(id = it.id)
                    )
                }
            )
            application { module(testSettings(repo)) }
            val client = createClient {
                install(ContentNegotiation) {
                    jackson {
                        setConfig(apiV1Mapper.serializationConfig)
                        setConfig(apiV1Mapper.deserializationConfig)
                    }
                }
            }
            val readRequest = PetAdGetRequest(
                requestType = "read",
                requestId = "123",
                debug = PetAdDebug(
                    mode = PetAdRequestDebugMode.STUB,
                    stub = PetAdRequestDebugStubs.SUCCESS
                ),
                petAd = PetAdGetObject(
                    id = "123",
                )
            )

            val response = client.post("/api/pet-ad/read") {
                contentType(ContentType.Application.Json)
                addAuthorization(id = "123", config = KtorAuthConfig.TEST, groups = listOf(CommonUserGroups.TEST.name))
                setBody(readRequest)
            }

            val petAdReadResponse = response.body<PetAdGetResponse>()
            response shouldHaveStatus HttpStatusCode.OK
            petAdReadResponse.petAd?.id shouldBe "123"
        }
    }

    test("delete success stub") {
        testApplication {
            val repo = AdRepositoryMock(
                invokeReadAd = {
                    DbAdResponse(
                        isSuccess = true,
                        data = PetAd(id = it.id)
                    )
                },
                invokeDeleteAd = {
                    DbAdResponse(
                        isSuccess = true,
                        data = PetAd(id = it.id)
                    )
                }
            )
            application { module(testSettings(repo)) }
            val client = createClient {
                install(ContentNegotiation) {
                    jackson {
                        setConfig(apiV1Mapper.serializationConfig)
                        setConfig(apiV1Mapper.deserializationConfig)
                    }
                }
            }
            val deleteRequest = PetAdDeleteRequest(
                requestType = "delete",
                requestId = "123",
                debug = PetAdDebug(
                    mode = PetAdRequestDebugMode.STUB,
                    stub = PetAdRequestDebugStubs.SUCCESS
                ),
                petAd = PetAdDeleteObject(
                    id = "123",
                )
            )

            val response = client.post("/api/pet-ad/delete") {
                contentType(ContentType.Application.Json)
                addAuthorization(id = "123", config = KtorAuthConfig.TEST, groups = listOf(CommonUserGroups.TEST.name))
                setBody(deleteRequest)
            }

            response shouldHaveStatus HttpStatusCode.OK
        }
    }

    test("search success stub") {
        testApplication {
            val repo = AdRepositoryMock(
                invokeSearchAd = {
                    DbAdsResponse(
                        isSuccess = true,
                        data = listOf(
                            PetAd(
                                petType = it.type,
                                temperament = it.temperament
                            )
                        )
                    )
                }
            )
            application { module(testSettings(repo)) }
            val client = createClient {
                install(ContentNegotiation) {
                    jackson {
                        setConfig(apiV1Mapper.serializationConfig)
                        setConfig(apiV1Mapper.deserializationConfig)
                    }
                }
            }

            val searchRequest = PetAdSearchRequest(
                requestType = "search",
                requestId = "123",
                debug = PetAdDebug(
                    mode = PetAdRequestDebugMode.STUB,
                    stub = PetAdRequestDebugStubs.SUCCESS
                ),
                petAdFilter = PetAdSearchFilter(
                    breed = "Unknown",
                    type = PetType.CAT.name,
                    temperament = PetTemperament.SANGUINE.name
                ),
            )
            val response = client.post("/api/pet-ad/search") {
                contentType(ContentType.Application.Json)
                addAuthorization(id = "123", config = KtorAuthConfig.TEST, groups = listOf(CommonUserGroups.TEST.name))
                setBody(searchRequest)
            }

            val petAdSearchResponse = response.body<PetAdSearchResponse>()
            val responseStub = PetAdStub.getPetAds()

            response shouldHaveStatus HttpStatusCode.OK
        }
    }

})