package ru.otus.otuskotlin.adoptabletails.mappers.mapper

import ru.otus.otuskotlin.adoptabletails.common.PetAdContext
import ru.otus.otuskotlin.adoptabletails.common.models.PetAdCommand
import ru.otus.otuskotlin.adoptabletails.common.models.PetAdError
import ru.otus.otuskotlin.adoptabletails.common.models.PetAdState
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAd
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAdId
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAdStatus
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetTemperament
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetType
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

fun PetAdContext.toTransportPetAd(): IResponse = when (val cmd = command) {
    PetAdCommand.CREATE -> toTransportPetAdCreate()
    PetAdCommand.READ -> toTransportPetAdRead()
    PetAdCommand.UPDATE -> toTransportPetAdUpdate()
    PetAdCommand.DELETE -> toTransportPetAdDelete()
    PetAdCommand.SEARCH -> toTransportPetAdSearch()
    PetAdCommand.NONE -> throw UnsupportedCommandException(cmd)
}

private fun PetAdContext.toTransportPetAdCreate() = PetAdCreateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == PetAdState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    petAd = petAdResponse.toTransportFullPetAd()
)

private fun PetAdContext.toTransportPetAdRead() = PetAdGetResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == PetAdState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    petAd = petAdResponse.toTransportFullPetAd()
)

private fun PetAdContext.toTransportPetAdUpdate() = PetAdUpdateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == PetAdState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    petAd = petAdResponse.toTransportFullPetAd()
)

private fun PetAdContext.toTransportPetAdDelete() = PetAdDeleteResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == PetAdState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    petAd = petAdResponse.toTransportDeletePetAd()
)

private fun PetAdContext.toTransportPetAdSearch() = PetAdSearchResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == PetAdState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
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
    petType = petType.takeIf { it != PetType.NONE }?.name,
    age = age.takeIf { it >= BigDecimal.ZERO },
    temperament = temperament.takeIf { it != PetTemperament.NONE }?.name,
    propertySize = size.takeIf { it.isNotBlank() },
    adStatus = petAdStatus.takeIf { it != PetAdStatus.NONE }?.name
)

private fun PetAd.toTransportDeletePetAd(): PetAdResponseDeleteObject = PetAdResponseDeleteObject(
    id = id.takeIf { it != PetAdId.NONE }?.asString()
)


private fun List<PetAdError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportError() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun PetAdError.toTransportError() = Error(
    code = code.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() }
)
