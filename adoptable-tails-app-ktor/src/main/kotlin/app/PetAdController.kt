package ru.otus.otuskotlin.adoptabletails.app.ktor.app

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.otus.otuskotlin.adoptabletails.common.PetAdContext
import ru.otus.otuskotlin.adoptabletails.mappers.mapper.fromTransportPetAd
import ru.otus.otuskotlin.adoptabletails.mappers.mapper.toTransportPetAd
import ru.otus.otuskotlin.adoptabletails.stubs.PetAdStub
import ru.otus.otuskotlin.api.models.PetAdCreateRequest
import ru.otus.otuskotlin.api.models.PetAdDeleteRequest
import ru.otus.otuskotlin.api.models.PetAdGetRequest
import ru.otus.otuskotlin.api.models.PetAdSearchRequest
import ru.otus.otuskotlin.api.models.PetAdUpdateRequest

suspend fun ApplicationCall.createPetAd() {
    val request = receive<PetAdCreateRequest>()
    val context = PetAdContext()
    context.fromTransportPetAd(request)
    context.petAdResponse = PetAdStub.getPetAd()
    respond(context.toTransportPetAd())
}

suspend fun ApplicationCall.readPetAd() {
    val request = receive<PetAdGetRequest>()
    val context = PetAdContext()
    context.fromTransportPetAd(request)
    context.petAdResponse = PetAdStub.getPetAd()
    respond(context.toTransportPetAd())
}

suspend fun ApplicationCall.updatePetAd() {
    val request = receive<PetAdUpdateRequest>()
    val context = PetAdContext()
    context.fromTransportPetAd(request)
    context.petAdResponse = PetAdStub.getPetAd()
    respond(context.toTransportPetAd())
}

suspend fun ApplicationCall.deletePetAd() {
    val request = receive<PetAdDeleteRequest>()
    val context = PetAdContext()
    context.fromTransportPetAd(request)
    respond(context.toTransportPetAd())
}

suspend fun ApplicationCall.searchPetAds() {
    val request = receive<PetAdSearchRequest>()
    val context = PetAdContext()
    context.fromTransportPetAd(request)
    context.petAdsResponse = PetAdStub.getPetAds()
    respond(context.toTransportPetAd())
}