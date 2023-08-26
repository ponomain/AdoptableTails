package ru.otus.otuskotlin.adoptabletails.biz.repository

import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsContext
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsState
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.CorChainDsl
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.handle
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.on
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.worker

fun CorChainDsl<AdoptableTailsContext>.repositoryPrepareDelete(title: String) = worker {
    this.title = title
    description = "Prepare data before deleting from DB"
    on { state == AdoptableTailsState.RUNNING }
    handle {
        adRepositoryPrepare = petAdValidated.deepCopy()
    }
}