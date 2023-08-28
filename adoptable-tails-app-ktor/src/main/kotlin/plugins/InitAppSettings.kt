package ru.otus.otuskotlin.adoptabletails.app.ktor.plugins

import io.ktor.server.application.*
import ru.otus.otuskotlin.adoptabletails.app.ktor.AdoptableTailsAppSettings
import ru.otus.otuskotlin.adoptabletails.app.ktor.configs.AdDbType
import ru.otus.otuskotlin.adoptabletails.app.ktor.configs.KtorAuthConfig
import ru.otus.otuskotlin.adoptabletails.biz.AdoptableTailsProcessor
import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsCorSettings
import ru.otus.otuskotlin.adoptabletails.lib.log.common.LogWrapper
import ru.otus.otuskotlin.adoptabletails.lib.log.common.LoggerProvider

fun Application.initAppSettings(): AdoptableTailsAppSettings {
    return AdoptableTailsAppSettings(
        corSettings = AdoptableTailsCorSettings(
            loggerProvider = LoggerProvider { LogWrapper.DEFAULT },
            repositoryTest = getDatabaseConf(AdDbType.TEST),
            repositoryProd = getDatabaseConf(AdDbType.PROD)
        ),
        adoptableTailsProcessor = AdoptableTailsProcessor()
    )
}

fun Application.getAuth(): KtorAuthConfig {
    val issuer = environment.config.propertyOrNull("jwt.issuer")?.getString() ?: ""
    val audience = environment.config.propertyOrNull("jwt.audience")?.getString() ?: ""
    val realm = environment.config.propertyOrNull("jwt.realm")?.getString() ?: ""
    val clientId = environment.config.propertyOrNull("jwt.clientId")?.getString() ?: ""
    val secret = environment.config.propertyOrNull("jwt.secret")?.getString() ?: ""
    val certUrl = environment.config.propertyOrNull("jwt.certUrl")?.getString() ?: ""
    return KtorAuthConfig(secret, issuer, audience, realm, clientId, certUrl)
}