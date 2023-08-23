package ru.otus.otuskotlin.adoptabletails.biz.validation

import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsContext
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsState
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.CorChainDsl
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.handle
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.on
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.worker

fun CorChainDsl<AdoptableTailsContext>.finishPetAdValidation(title: String) = worker {
    this.title = title
    on { state == AdoptableTailsState.RUNNING }
    handle {
        petAdValidated = petAdValidating
    }
}

fun CorChainDsl<AdoptableTailsContext>.finishPetAdFilterValidation(title: String) = worker {
    this.title = title
    on { state == AdoptableTailsState.RUNNING }
    handle {
        petAdFilterValidated = petAdFilterValidating
    }
}