package ru.otus.otuskotlin.adoptabletails.mappers.mapper

import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsContext
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsCommand
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsError
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsState
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAd
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAdId
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAdStatus
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetTemperament
import ru.otus.otuskotlin.adoptabletails.mappers.exception.UnsupportedCommandException
import ru.otus.otuskotlin.api.models.Error
import ru.otus.otuskotlin.api.models.IResponse
import ru.otus.otuskotlin.api.models.PetAdCreateResponse
import ru.otus.otuskotlin.api.models.PetAdDeleteResponse
import ru.otus.otuskotlin.api.models.PetAdGetResponse
import ru.otus.otuskotlin.api.models.PetAdResponseDeleteObject
import ru.otus.otuskotlin.api.models.PetAdResponseFullObject
import ru.otus.otuskotlin.api.models.PetAdSearchResponse
import ru.otus.otuskotlin.api.models.PetAdUpdateResponse
import ru.otus.otuskotlin.api.models.ResponseResult
import java.math.BigDecimal
import java.time.Instant

fun AdoptableTailsContext.toTransportPetAd(): IResponse = when (val cmd = command) {
    AdoptableTailsCommand.CREATE -> toTransportPetAdCreate()
    AdoptableTailsCommand.READ -> toTransportPetAdRead()
    AdoptableTailsCommand.UPDATE -> toTransportPetAdUpdate()
    AdoptableTailsCommand.DELETE -> toTransportPetAdDelete()
    AdoptableTailsCommand.SEARCH -> toTransportPetAdSearch()
    AdoptableTailsCommand.NONE -> throw UnsupportedCommandException(cmd)
}

private fun AdoptableTailsContext.toTransportPetAdCreate() = PetAdCreateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == AdoptableTailsState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    petAd = petAdResponse.toTransportFullPetAd()
)

private fun AdoptableTailsContext.toTransportPetAdRead() = PetAdGetResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == AdoptableTailsState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    petAd = petAdResponse.toTransportFullPetAd()
)

private fun AdoptableTailsContext.toTransportPetAdUpdate() = PetAdUpdateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == AdoptableTailsState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    petAd = petAdResponse.toTransportFullPetAd()
)

private fun AdoptableTailsContext.toTransportPetAdDelete() = PetAdDeleteResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == AdoptableTailsState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    petAd = petAdResponse.toTransportDeletePetAd()
)

private fun AdoptableTailsContext.toTransportPetAdSearch() = PetAdSearchResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == AdoptableTailsState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    petAds = petAdsResponse.toTransportFullPetAds()
)

private fun List<PetAd>.toTransportFullPetAds(): List<PetAdResponseFullObject>? = this
    .map { it.toTransportFullPetAd() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun PetAd.toTransportFullPetAd(): PetAdResponseFullObject = PetAdResponseFullObject(
    id = id.takeIf { it != PetAdId.NONE }?.asString(),
    description = description.takeIf { it.isNotBlank() },
    name = name.takeIf { it.isNotBlank() },
    breed = breed.takeIf { it.isNotBlank() },
    age = age.takeIf { it >= BigDecimal.ZERO },
    temperament = temperament.takeIf { it != PetTemperament.NONE }?.name,
    propertySize = size.takeIf { it.isNotBlank() },
    adStatus = petAdStatus.takeIf { it != PetAdStatus.NONE }?.name,
    createdAd = createdAt.takeIf { it != Instant.MIN }.toString(),
    updatedAt = updatedAt.takeIf { it != Instant.MIN }.toString()
)

private fun PetAd.toTransportDeletePetAd(): PetAdResponseDeleteObject = PetAdResponseDeleteObject(
    id = id.takeIf { it != PetAdId.NONE }?.asString()
)


private fun List<AdoptableTailsError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportError() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun AdoptableTailsError.toTransportError() = Error(
    code = code.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() }
)
