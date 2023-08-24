package ru.otus.otuskotlin.adoptabletails.app.ktor.authorization
import io.ktor.client.request.*
import io.ktor.server.testing.*
import org.junit.Test
import ru.otus.otuskotlin.adoptabletails.app.ktor.configs.KtorAuthConfig
import ru.otus.otuskotlin.adoptabletails.app.ktor.module
import ru.otus.otuskotlin.adoptabletails.app.ktor.testSettings
import kotlin.test.assertEquals
class AuthorizationTest {
    @Test
    fun invalidAudience() = testApplication {
        application { module(testSettings()) }


        val response = client.post("api/v1/pet-ad/create") {
            addAuthorization(id = "test", config = KtorAuthConfig.TEST.copy(audience = "invalid"), groups = listOf())
        }

        assertEquals(401, response.status.value)
    }
}