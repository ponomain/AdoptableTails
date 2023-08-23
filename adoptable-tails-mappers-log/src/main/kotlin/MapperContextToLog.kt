package ru.otus.otuskotlin.adoptabletails.mappers.log

import kotlinx.datetime.Clock
import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsContext
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsError
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsRequestId
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAd
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAdFilter
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAdId
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetTemperament
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetType
import ru.otus.otuskotlin.api.log.models.CommonLogModel
import ru.otus.otuskotlin.api.log.models.ErrorLogModel
import ru.otus.otuskotlin.api.log.models.PetAdFilterLog
import ru.otus.otuskotlin.api.log.models.PetAdLog
import ru.otus.otuskotlin.api.log.models.PetAdLogModel

fun AdoptableTailsContext.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "down-work",
    prgrp = toLog(),
    errors = errors.map { it.toLog() },

    )

fun AdoptableTailsContext.toLog(): PetAdLogModel? {
    val petAdNone = PetAd()
    return PetAdLogModel(
        requestId = requestId.takeIf { it != AdoptableTailsRequestId.NONE }?.asString(),
        requestPetAd = petAdRequest.takeIf { it != petAdNone }?.toLog(),
        responsePetAd = petAdRequest.takeIf { it != petAdNone }?.toLog(),
        responsePetAds = petAdsResponse
            .takeIf { it.isNotEmpty() }
            ?.filter { it != petAdNone }
            ?.map { it.toLog() },
        requestFilter = petAdFilter.takeIf { it != PetAdFilter() }?.toLog(),
    ).takeIf { it != PetAdLogModel() }
}

private fun PetAdFilter.toLog() = PetAdFilterLog(
    petType = type.takeIf { it != PetType.NONE }.toString(),
    temperament = temperament.takeIf { it != PetTemperament.NONE }.toString()
)

fun AdoptableTailsError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() }
)

fun PetAd.toLog() = PetAdLog(
    id = id.takeIf { it != PetAdId.NONE }?.asString(),
    name = name.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() }
)