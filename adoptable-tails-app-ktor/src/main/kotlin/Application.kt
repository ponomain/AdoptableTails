package ru.otus.otuskotlin.adoptabletails.app.ktor

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.netty.*

import io.ktor.server.routing.*
import ru.otus.otuskotlin.adoptabletails.app.ktor.app.petAd
import ru.otus.otuskotlin.adoptabletails.app.ktor.configs.KtorAuthConfig
import ru.otus.otuskotlin.adoptabletails.app.ktor.plugins.getAuth
import ru.otus.otuskotlin.adoptabletails.app.ktor.plugins.initAppSettings
import ru.otus.otuskotlin.adoptabletails.app.ktor.plugins.initPlugins

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Suppress("unused")
fun Application.module(
    appSettings: AdoptableTailsAppSettings = initAppSettings(),
    authSettings: KtorAuthConfig = getAuth()
) {
    initPlugins(appSettings, authSettings)

    routing {
        authenticate("auth-jwt") {
            route("api") {
                petAd(appSettings)
            }
        }
    }
}