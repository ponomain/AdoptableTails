package ru.otus.otuskotlin.adoptabletails.biz.workers.stubs

import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsContext
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsError
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsState
import ru.otus.otuskotlin.adoptabletails.common.stubs.PetAdStubs
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.CorChainDsl
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.handle
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.on
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.worker

fun CorChainDsl<AdoptableTailsContext>.stubValidationBadBreed(title: String) = worker {
    this.title = title
    on { state == AdoptableTailsState.RUNNING && stub == PetAdStubs.BAD_BREED }
    handle {
        state = AdoptableTailsState.FAILING
        errors.add(
            AdoptableTailsError(
                group = "validation",
                code = "validation-id",
                field = "breed",
                message = "Wrong breed field",
            )
        )
    }
}