package ru.otus.otuskotlin.adoptabletails.biz.validation

import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsContext
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsError
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsState
import ru.otus.otuskotlin.adoptabletails.common.models.helpers.addError
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.CorChainDsl
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.handle
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.on
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.worker

fun CorChainDsl<AdoptableTailsContext>.validatePetAdName(title: String) = worker {
    this.title = title
    on { petAdValidating.name.length > 20 }
    handle {
        addError(
            AdoptableTailsError(
                group = "validation",
                code = "validation-id",
                field = "name",
                message = "Wrong name, length must not exceed 20 characters"
            )
        )
        state = AdoptableTailsState.FAILING
    }
}