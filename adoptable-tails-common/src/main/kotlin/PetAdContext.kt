package ru.otus.otuskotlin.adoptabletails.common

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.adoptabletails.common.models.PetAdCommand
import ru.otus.otuskotlin.adoptabletails.common.models.PetAdError
import ru.otus.otuskotlin.adoptabletails.common.models.PetAdRequestId
import ru.otus.otuskotlin.adoptabletails.common.models.PetAdState
import ru.otus.otuskotlin.adoptabletails.common.models.PetAdWorkMode
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAd
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAdFilter
import ru.otus.otuskotlin.adoptabletails.common.stubs.PetAdStubs

data class PetAdContext (
    var command : PetAdCommand = PetAdCommand.NONE,
    var state : PetAdState = PetAdState.NONE,
    var errors :MutableList<PetAdError> = mutableListOf(),

    var workMode : PetAdWorkMode = PetAdWorkMode.PROD,
    var stub: PetAdStubs = PetAdStubs.NONE,
    var requestId: PetAdRequestId = PetAdRequestId.NONE,
    var timeStart : Instant = Instant.NONE,

    var petAdFilter: PetAdFilter = PetAdFilter(),
    var petAdRequest : PetAd = PetAd(),

    var petAdResponse :PetAd = PetAd(),
    var petAdsResponse :MutableList<PetAd> = mutableListOf()
)