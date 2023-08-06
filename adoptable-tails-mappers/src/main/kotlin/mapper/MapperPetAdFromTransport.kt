package ru.otus.otuskotlin.adoptabletails.mappers.mapper

import ru.otus.otuskotlin.adoptabletails.common.PetAdContext
import ru.otus.otuskotlin.adoptabletails.common.models.PetAdCommand
import ru.otus.otuskotlin.adoptabletails.common.models.PetAdRequestId
import ru.otus.otuskotlin.adoptabletails.common.models.PetAdWorkMode
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAd
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAdFilter
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAdId
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAdStatus
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetTemperament
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetType
import ru.otus.otuskotlin.adoptabletails.common.stubs.PetAdStubs
import ru.otus.otuskotlin.adoptabletails.mappers.exception.UnsupportedClassException
import ru.otus.otuskotlin.api.models.IRequest
import ru.otus.otuskotlin.api.models.PetAdCreateObject
import ru.otus.otuskotlin.api.models.PetAdCreateRequest
import ru.otus.otuskotlin.api.models.PetAdDebug
import ru.otus.otuskotlin.api.models.PetAdDeleteRequest
import ru.otus.otuskotlin.api.models.PetAdGetRequest
import ru.otus.otuskotlin.api.models.PetAdRequestDebugMode
import ru.otus.otuskotlin.api.models.PetAdRequestDebugStubs
import ru.otus.otuskotlin.api.models.PetAdSearchFilter
import ru.otus.otuskotlin.api.models.PetAdSearchRequest
import ru.otus.otuskotlin.api.models.PetAdUpdateObject
import ru.otus.otuskotlin.api.models.PetAdUpdateRequest
import java.math.BigDecimal

fun PetAdContext.fromTransportPetAd(request: IRequest) = when (request) {
    is PetAdCreateRequest -> fromTransportPetAd(request)
    is PetAdGetRequest -> fromTransportPetAd(request)
    is PetAdUpdateRequest -> fromTransportPetAd(request)
    is PetAdDeleteRequest -> fromTransportPetAd(request)
    is PetAdSearchRequest -> fromTransportPetAd(request)
    else -> throw UnsupportedClassException(request.javaClass)
}

private fun String?.toPetAdId() = this?.let { PetAdId(it) } ?: PetAdId.NONE
private fun String?.toPetAdWithId() = PetAd(id = this.toPetAdId())
private fun IRequest?.requestId() = this?.requestId?.let { PetAdRequestId(it) } ?: PetAdRequestId.NONE

private fun PetAdDebug?.transportPetAdToWorkMode(): PetAdWorkMode = when (this?.mode) {
    PetAdRequestDebugMode.PROD -> PetAdWorkMode.PROD
    PetAdRequestDebugMode.STUB -> PetAdWorkMode.STUB
    PetAdRequestDebugMode.TEST -> PetAdWorkMode.TEST
    null -> PetAdWorkMode.PROD
}

private fun PetAdDebug?.transportPetAdToStubCase(): PetAdStubs = when (this?.stub) {
    PetAdRequestDebugStubs.SUCCESS -> PetAdStubs.SUCCESS
    PetAdRequestDebugStubs.CANNOT_CREATE -> PetAdStubs.CANNOT_CREATE
    PetAdRequestDebugStubs.CANNOT_DELETE -> PetAdStubs.CANNOT_DELETE
    PetAdRequestDebugStubs.CANNOT_READ -> PetAdStubs.CANNOT_READ
    PetAdRequestDebugStubs.CANNOT_SEARCH -> PetAdStubs.CANNOT_SEARCH
    PetAdRequestDebugStubs.CANNOT_UPDATE -> PetAdStubs.CANNOT_UPDATE
    null -> PetAdStubs.NONE
}

private fun PetAdContext.fromTransportPetAd(request: PetAdCreateRequest) = this.copy(
    command = PetAdCommand.CREATE,
    requestId = request.requestId(),
    petAdRequest = request.petAd?.toInternal() ?: PetAd(),
    workMode = request.debug.transportPetAdToWorkMode(),
    stub = request.debug.transportPetAdToStubCase()
)

private fun PetAdContext.fromTransportPetAd(request: PetAdGetRequest) = this.copy(
    command = PetAdCommand.READ,
    requestId = request.requestId(),
    petAdRequest = request.petAd?.id.toPetAdWithId(),
    workMode = request.debug.transportPetAdToWorkMode(),
    stub = request.debug.transportPetAdToStubCase()
)

private fun PetAdContext.fromTransportPetAd(request: PetAdUpdateRequest) = this.copy(
    command = PetAdCommand.UPDATE,
    requestId = request.requestId(),
    petAdRequest = request.petAd?.toInternal() ?: PetAd(),
    workMode = request.debug.transportPetAdToWorkMode(),
    stub = request.debug.transportPetAdToStubCase()
)

private fun PetAdContext.fromTransportPetAd(request: PetAdDeleteRequest) = this.copy(
    command = PetAdCommand.DELETE,
    requestId = request.requestId(),
    petAdRequest = request.petAd?.id.toPetAdWithId(),
    workMode = request.debug.transportPetAdToWorkMode(),
    stub = request.debug.transportPetAdToStubCase()
)

private fun PetAdContext.fromTransportPetAd(request: PetAdSearchRequest) = this.copy(
    command = PetAdCommand.SEARCH,
    requestId = request.requestId(),
    petAdFilter = request.petAdFilter.toInternal(),
    workMode = request.debug.transportPetAdToWorkMode(),
    stub = request.debug.transportPetAdToStubCase()
)


private fun PetAdSearchFilter?.toInternal(): PetAdFilter = PetAdFilter(
    breed = this?.breed ?: "",
    type = this?.type.fromTransportType(),
    temperament = this?.temperament.fromTransportTemperament()
)

private fun String?.fromTransportType(): PetType = when (this) {
    "DOG" -> PetType.DOG
    "CAT" -> PetType.CAT
    else -> PetType.NONE
}

private fun String?.fromTransportTemperament(): PetTemperament = when (this) {
    "SANGUINE" -> PetTemperament.SANGUINE
    "CHOLERIC" -> PetTemperament.CHOLERIC
    "PHLEGMATIC" -> PetTemperament.PHLEGMATIC
    "MELANCHOLIC" -> PetTemperament.MELANCHOLIC
    else -> PetTemperament.NONE
}

private fun PetAdCreateObject.toInternal(): PetAd = PetAd(
    name = this.name ?: "",
    description = this.description ?: "",
    breed = this.breed ?: "",
    petType = this.petType.fromTransportType(),
    temperament = this.temperament.fromTransportTemperament(),
    size = this.propertySize ?: "",
    age = this.age ?: BigDecimal.ZERO,
)

private fun String?.fromTransportStatus(): PetAdStatus = when (this) {
    "CREATED" -> PetAdStatus.CREATED
    "RESERVED" -> PetAdStatus.RESERVED
    "ADOPTED" -> PetAdStatus.ADOPTED
    else -> PetAdStatus.NONE
}

private fun PetAdUpdateObject.toInternal(): PetAd = PetAd(
    petAdStatus = this.adStatus.fromTransportStatus()
)
