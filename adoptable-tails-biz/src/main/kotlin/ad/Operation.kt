package ru.otus.otuskotlin.adoptabletails.biz.ad

import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsContext
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsCommand
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsState
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.CorChainDsl
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.chain
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.on

fun <T : AdoptableTailsContext> CorChainDsl<T>.operation(
    title: String,
    command: AdoptableTailsCommand,
    block: CorChainDsl<T>.() -> Unit
) = chain {
    this.title = title
    on { this.command == command && this.state == AdoptableTailsState.RUNNING }
    //on { true }
    block()
}