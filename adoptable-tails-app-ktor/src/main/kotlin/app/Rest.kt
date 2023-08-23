package ru.otus.otuskotlin.adoptabletails.app.ktor.app

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.otus.otuskotlin.adoptabletails.app.ktor.AdoptableTailsAppSettings

fun Route.petAd(appSettings: AdoptableTailsAppSettings) {
    val logger = appSettings.corSettings.loggerProvider.logger(Route::petAd)
    route("pet-ad") {
        post("create") {
            call.createPetAd(logger, appSettings)
        }
        post("read") {
            call.readPetAd(logger, appSettings)
        }
        post("update") {
            call.updatePetAd(logger, appSettings)
        }
        post("delete") {
            call.deletePetAd(logger, appSettings)
        }
        post("search") {
            call.searchPetAds(logger, appSettings)
        }
    }
}