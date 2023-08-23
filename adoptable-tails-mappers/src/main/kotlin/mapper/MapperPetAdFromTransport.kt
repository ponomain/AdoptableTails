package ru.otus.otuskotlin.adoptabletails.mappers.mapper

import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsContext
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsCommand
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsRequestId
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsWorkMode
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

fun AdoptableTailsContext.fromTransportPetAd(request: IRequest) = when (request) {
    is PetAdCreateRequest -> fromTransportPetAd(request)
    is PetAdGetRequest -> fromTransportPetAd(request)
    is PetAdUpdateRequest -> fromTransportPetAd(request)
    is PetAdDeleteRequest -> fromTransportPetAd(request)
    is PetAdSearchRequest -> fromTransportPetAd(request)
    else -> throw UnsupportedClassException(request.javaClass)
}

private fun String?.toPetAdId() = this?.let { PetAdId(it) } ?: PetAdId.NONE
private fun String?.toPetAdWithId() = PetAd(id = this.toPetAdId())
private fun IRequest?.requestId() = this?.requestId?.let { AdoptableTailsRequestId(it) } ?: AdoptableTailsRequestId.NONE

private fun PetAdDebug?.transportPetAdToWorkMode(): AdoptableTailsWorkMode = when (this?.mode) {
    PetAdRequestDebugMode.PROD -> AdoptableTailsWorkMode.PROD
    PetAdRequestDebugMode.STUB -> AdoptableTailsWorkMode.STUB
    PetAdRequestDebugMode.TEST -> AdoptableTailsWorkMode.TEST
    null -> AdoptableTailsWorkMode.PROD
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

private fun AdoptableTailsContext.fromTransportPetAd(request: PetAdCreateRequest) = this.copy(
    command = AdoptableTailsCommand.CREATE,
    requestId = request.requestId(),
    petAdRequest = request.petAd?.toInternal() ?: PetAd(),
    workMode = request.debug.transportPetAdToWorkMode(),
    stub = request.debug.transportPetAdToStubCase()
)

private fun AdoptableTailsContext.fromTransportPetAd(request: PetAdGetRequest) = this.copy(
    command = AdoptableTailsCommand.READ,
    requestId = request.requestId(),
    petAdRequest = request.petAd?.id.toPetAdWithId(),
    workMode = request.debug.transportPetAdToWorkMode(),
    stub = request.debug.transportPetAdToStubCase()
)

private fun AdoptableTailsContext.fromTransportPetAd(request: PetAdUpdateRequest) = this.copy(
    command = AdoptableTailsCommand.UPDATE,
    requestId = request.requestId(),
    petAdRequest = request.petAd?.toInternal() ?: PetAd(),
    workMode = request.debug.transportPetAdToWorkMode(),
    stub = request.debug.transportPetAdToStubCase()
)

private fun AdoptableTailsContext.fromTransportPetAd(request: PetAdDeleteRequest) = this.copy(
    command = AdoptableTailsCommand.DELETE,
    requestId = request.requestId(),
    petAdRequest = request.petAd?.id.toPetAdWithId(),
    workMode = request.debug.transportPetAdToWorkMode(),
    stub = request.debug.transportPetAdToStubCase()
)

private fun AdoptableTailsContext.fromTransportPetAd(request: PetAdSearchRequest) = this.copy(
    command = AdoptableTailsCommand.SEARCH,
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
