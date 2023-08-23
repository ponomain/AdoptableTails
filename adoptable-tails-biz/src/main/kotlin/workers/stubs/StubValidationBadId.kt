package ru.otus.otuskotlin.adoptabletails.biz.workers.stubs

import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsContext
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsError
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsState
import ru.otus.otuskotlin.adoptabletails.common.stubs.PetAdStubs
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.CorChainDsl
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.handle
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.on
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.worker

fun CorChainDsl<AdoptableTailsContext>.stubValidationBadId(title: String) = worker {
    this.title = title
    on { state == AdoptableTailsState.RUNNING && stub == PetAdStubs.BAD_ID }
    handle {
        state = AdoptableTailsState.FAILING
        errors.add(
            AdoptableTailsError(
                group = "validation",
                code = "validation-id",
                field = "id",
                message = "Wrong id field",
            )
        )
    }
}