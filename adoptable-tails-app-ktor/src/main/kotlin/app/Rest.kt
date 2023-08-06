package ru.otus.otuskotlin.adoptabletails.app.ktor.app

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.petAd() {
    route("pet-ad") {
        post("create") {
            call.createPetAd()
        }
        post("read") {
            call.readPetAd()
        }
        post("update") {
            call.updatePetAd()
        }
        post("delete") {
            call.deletePetAd()
        }
        post("search") {
            call.searchPetAds()
        }
    }
}