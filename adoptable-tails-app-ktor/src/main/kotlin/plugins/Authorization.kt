package ru.otus.otuskotlin.adoptabletails.app.ktor.plugins

import com.auth0.jwk.Jwk
import com.auth0.jwk.UrlJwkProvider
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.auth.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import ru.otus.otuskotlin.adoptabletails.app.ktor.AdoptableTailsAppSettings
import ru.otus.otuskotlin.adoptabletails.app.ktor.configs.KtorAuthConfig
import ru.otus.otuskotlin.adoptabletails.app.ktor.configs.KtorAuthConfig.Companion.GROUPS_CLAIM
import java.net.URL
import java.security.interfaces.RSAPublicKey

fun Application.configureAuthorization(appConfigs: AdoptableTailsAppSettings) {
    install(Authentication) {
        jwt("auth-jwt") {
            val authorizationConfig = appConfigs.authorization
            realm = authorizationConfig.realm

            verifier {
                val algorithm = it.resolveAlgorithm(authorizationConfig)
                JWT.require(Algorithm.none())
                    .withAudience(authorizationConfig.audience)
                    .withIssuer(authorizationConfig.issuer)
                    .build()
            }
            validate { jwtCredential: JWTCredential ->
                when {
                    jwtCredential.payload.getClaim(GROUPS_CLAIM).asList(String::class.java).isNullOrEmpty() -> {
                        null
                    }
                    else -> JWTPrincipal(jwtCredential.payload)
                }
            }
        }
    }
}

fun HttpAuthHeader.resolveAlgorithm(authConfig: KtorAuthConfig): Algorithm = when {
    authConfig.certUrl != null -> resolveAlgorithmKeycloak(authConfig)
    else -> Algorithm.HMAC256(authConfig.secret)
}

private val jwks = mutableMapOf<String, Jwk>()

fun HttpAuthHeader.resolveAlgorithmKeycloak(authConfig: KtorAuthConfig): Algorithm {
    val tokenString = this.render().replace(this.authScheme, "").trim()
    if (tokenString.isBlank()) {
        throw IllegalArgumentException("Request contains no proper Authorization header")
    }
    val token = try {
        JWT.decode(tokenString)
    } catch (e: Exception) {
        throw IllegalArgumentException("Cannot parse JWT token from request", e)
    }
    val algo = token.algorithm
    if (algo != "RS256") {
        throw IllegalArgumentException("Wrong algorithm in JWT ($algo). Must be ...")
    }
    val keyId = token.keyId
    val jwk = jwks[keyId] ?: run {
        val provider = UrlJwkProvider(URL(authConfig.certUrl))
        val jwk = provider.get(keyId)
        jwks[keyId] = jwk
        jwk
    }
    val publicKey = jwk.publicKey
    if (publicKey !is RSAPublicKey) {
        throw IllegalArgumentException("Key with ID was found in JWKS but is not a RSA-key.")
    }
    return Algorithm.RSA256(publicKey, null)
}