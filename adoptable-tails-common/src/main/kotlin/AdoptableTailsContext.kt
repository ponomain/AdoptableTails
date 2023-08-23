package ru.otus.otuskotlin.adoptabletails.common

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsCommand
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsError
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsRequestId
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsState
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsWorkMode
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAd
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAdFilter
import ru.otus.otuskotlin.adoptabletails.common.stubs.PetAdStubs

data class AdoptableTailsContext (
    var command : AdoptableTailsCommand = AdoptableTailsCommand.NONE,
    var state : AdoptableTailsState = AdoptableTailsState.NONE,
    var errors :MutableList<AdoptableTailsError> = mutableListOf(),

    var workMode : AdoptableTailsWorkMode = AdoptableTailsWorkMode.PROD,
    var stub: PetAdStubs = PetAdStubs.NONE,
    var requestId: AdoptableTailsRequestId = AdoptableTailsRequestId.NONE,
    var timeStart : Instant = Instant.NONE,

    var petAdFilter: PetAdFilter = PetAdFilter(),
    var petAdRequest : PetAd = PetAd(),

    var petAdResponse :PetAd = PetAd(),
    var petAdsResponse :MutableList<PetAd> = mutableListOf(),

    var petAdValidating: PetAd = PetAd(),
    var petAdFilterValidating: PetAdFilter = PetAdFilter(),

    var petAdValidated: PetAd = PetAd(),
    var petAdFilterValidated: PetAdFilter = PetAdFilter(),
)