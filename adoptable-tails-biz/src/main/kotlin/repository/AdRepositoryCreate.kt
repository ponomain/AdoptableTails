package ru.otus.otuskotlin.adoptabletails.biz.repository

import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsContext
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsState
import ru.otus.otuskotlin.adoptabletails.common.repository.DbAdRequest
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.CorChainDsl
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.handle
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.on
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.worker

fun CorChainDsl<AdoptableTailsContext>.repositoryCreate(title: String) = worker {
    this.title = title
    description = "Inserting object into the DB"
    on { state == AdoptableTailsState.RUNNING }
    handle {
        val request = DbAdRequest(adRepositoryPrepare)
        val result = adRepository.createAd(request)
        val resultOrder = result.data
        if (result.isSuccess && resultOrder != null) {
            adRepositoryDone = resultOrder
        } else {
            state = AdoptableTailsState.FAILING
            errors.addAll(result.errors)
        }
    }
}