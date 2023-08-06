package ru.otus.otuskotlin.adoptabletails.mappers.mapper

import ru.otus.otuskotlin.adoptabletails.common.models.PetAdWorkMode
import ru.otus.otuskotlin.adoptabletails.common.stubs.PetAdStubs
import ru.otus.otuskotlin.api.models.PetAdDebug
import ru.otus.otuskotlin.api.models.PetAdRequestDebugMode
import ru.otus.otuskotlin.api.models.PetAdRequestDebugStubs

fun PetAdDebug?.transportToWorkMode(): PetAdWorkMode = when (this?.mode) {
    PetAdRequestDebugMode.STUB -> PetAdWorkMode.STUB
    PetAdRequestDebugMode.TEST -> PetAdWorkMode.TEST
    PetAdRequestDebugMode.PROD -> PetAdWorkMode.PROD
    null -> PetAdWorkMode.PROD
}

fun PetAdDebug?.transportToStubCase(): PetAdStubs = when (this?.stub) {
    PetAdRequestDebugStubs.CANNOT_CREATE -> PetAdStubs.CANNOT_CREATE
    PetAdRequestDebugStubs.CANNOT_DELETE -> PetAdStubs.CANNOT_DELETE
    PetAdRequestDebugStubs.CANNOT_UPDATE -> PetAdStubs.CANNOT_UPDATE
    PetAdRequestDebugStubs.CANNOT_SEARCH -> PetAdStubs.CANNOT_SEARCH
    PetAdRequestDebugStubs.CANNOT_READ -> PetAdStubs.CANNOT_READ
    PetAdRequestDebugStubs.SUCCESS -> PetAdStubs.SUCCESS
    null -> PetAdStubs.NONE
}