package ru.otus.otuskotlin.adoptabletails.app.ktor.app

import io.ktor.server.application.*
import ru.otus.otuskotlin.adoptabletails.app.ktor.AdoptableTailsAppSettings
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsCommand
import ru.otus.otuskotlin.adoptabletails.lib.log.common.LogWrapper
import ru.otus.otuskotlin.api.models.PetAdCreateRequest
import ru.otus.otuskotlin.api.models.PetAdDeleteRequest
import ru.otus.otuskotlin.api.models.PetAdGetRequest
import ru.otus.otuskotlin.api.models.PetAdSearchRequest
import ru.otus.otuskotlin.api.models.PetAdUpdateRequest

suspend fun ApplicationCall.createPetAd(log: LogWrapper, appSettings: AdoptableTailsAppSettings) =
    petAdProcess<PetAdCreateRequest>(appSettings, log, "pet-ad-create", AdoptableTailsCommand.CREATE)

suspend fun ApplicationCall.readPetAd(log: LogWrapper, appSettings: AdoptableTailsAppSettings) =
    petAdProcess<PetAdGetRequest>(appSettings, log, "pet-ad-read", AdoptableTailsCommand.READ)

suspend fun ApplicationCall.updatePetAd(log: LogWrapper, appSettings: AdoptableTailsAppSettings) =
    petAdProcess<PetAdUpdateRequest>(appSettings, log, "pet-ad-update", AdoptableTailsCommand.UPDATE)

suspend fun ApplicationCall.deletePetAd(log: LogWrapper, appSettings: AdoptableTailsAppSettings) =
    petAdProcess<PetAdDeleteRequest>(appSettings, log, "pet-ad-delete", AdoptableTailsCommand.DELETE)

suspend fun ApplicationCall.searchPetAds(log: LogWrapper, appSettings: AdoptableTailsAppSettings) =
    petAdProcess<PetAdSearchRequest>(appSettings, log, "pet-ad-search", AdoptableTailsCommand.SEARCH)
