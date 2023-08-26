package ru.otus.otuskotlin.adoptabletails.app.ktor.plugins

import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import org.slf4j.event.Level
import ru.otus.otuskotlin.adoptabletails.api.apiV1Mapper
import ru.otus.otuskotlin.adoptabletails.app.ktor.AdoptableTailsAppSettings
import ru.otus.otuskotlin.adoptabletails.app.ktor.module
import ru.otus.otuskotlin.adoptabletails.lib.logback.LogWrapperLogback

private val clazz = Application::module::class.qualifiedName ?: "Application"

fun Application.initPlugins(appSettings: AdoptableTailsAppSettings) {

    install(CachingHeaders)
    install(DefaultHeaders)
    install(AutoHeadResponse)
    install(CORS)
    configureAuthorization(appSettings)
    install(ContentNegotiation) {
        jackson {
            setConfig(apiV1Mapper.serializationConfig)
            setConfig(apiV1Mapper.deserializationConfig)
        }
    }

    install(CallLogging) {
        level = Level.INFO
        val wrapper = appSettings
            .corSettings
            .loggerProvider
            .logger(clazz) as? LogWrapperLogback
        wrapper?.logger?.also { logger = it }
    }
}