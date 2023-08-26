package ru.otus.otuskotlin.adoptabletails.stubs

import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsContext
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsError
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAd

fun <T : AdoptableTailsContext> T.addError(group: String, code: String, field: String, message: String) {
    this.errors.add(AdoptableTailsError(group, code, field, message))
}


fun PetAdStub.createMeeting(block: PetAd.() -> Unit) = getPetAd().deepCopy().apply { block() }

fun PetAdStub.updateMeeting(block: PetAd.() -> Unit) = getPetAd().deepCopy().apply { block() }