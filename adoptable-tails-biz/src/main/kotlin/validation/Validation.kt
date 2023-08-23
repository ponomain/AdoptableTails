package ru.otus.otuskotlin.adoptabletails.biz.validation

import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsContext
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsState
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.CorChainDsl
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.chain
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.on

fun CorChainDsl<AdoptableTailsContext>.validation(block: CorChainDsl<AdoptableTailsContext>.() -> Unit) = chain {
    block()
    title = "validation"
    on { state == AdoptableTailsState.RUNNING }
}