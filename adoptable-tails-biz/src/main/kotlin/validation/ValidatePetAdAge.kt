package ru.otus.otuskotlin.adoptabletails.biz.validation

import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsContext
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsError
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsState
import ru.otus.otuskotlin.adoptabletails.common.models.helpers.addError
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.CorChainDsl
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.handle
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.on
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.worker
import java.math.BigDecimal

fun CorChainDsl<AdoptableTailsContext>.validatePetAdAge(title: String) = worker {
    this.title = title
    on { petAdValidating.age < BigDecimal.ZERO }
    handle {
        addError(
            AdoptableTailsError(
                group = "validation",
                code = "validation-id",
                field = "age",
                message = "Wrong age. Age cannot be less than 0"
            )
        )
        state = AdoptableTailsState.FAILING
    }
}