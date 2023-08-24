package ru.otus.otuskotlin.adoptabletails.biz.repository

import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsContext
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsState
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.CorChainDsl
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.handle
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.on
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.worker

fun CorChainDsl<AdoptableTailsContext>.repositoryPrepareCreate(title: String) = worker {
    this.title = title
    description = "Prepare object before saving into the DB"
    on { state == AdoptableTailsState.RUNNING }
    handle {
        adRepositoryRead = petAdValidated.copy()
        adRepositoryPrepare = adRepositoryRead
    }
}