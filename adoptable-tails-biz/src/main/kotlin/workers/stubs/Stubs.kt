package ru.otus.otuskotlin.adoptabletails.biz.workers.stubs

import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsContext
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsState
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsWorkMode
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.CorChainDsl
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.chain
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.on

fun <T : AdoptableTailsContext> CorChainDsl<T>.stubs(title: String, block: CorChainDsl<T>.() -> Unit) = chain {
    this.title = title
    on { state == AdoptableTailsState.RUNNING && workMode == AdoptableTailsWorkMode.STUB }
    block()
}