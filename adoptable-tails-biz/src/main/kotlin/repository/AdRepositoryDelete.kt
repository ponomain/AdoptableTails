package ru.otus.otuskotlin.adoptabletails.biz.repository

import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsContext
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsState
import ru.otus.otuskotlin.adoptabletails.common.repository.DbAdIdRequest
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.CorChainDsl
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.handle
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.on
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.worker

fun CorChainDsl<AdoptableTailsContext>.repositoryDelete(title: String) = worker {
    this.title = title
    description = "Deletion of object by id from the DB"
    on { state == AdoptableTailsState.RUNNING }
    handle {
        val request = DbAdIdRequest(adRepositoryPrepare)
        val result = adRepository.deleteAd(request)
        if (!result.isSuccess) {
            state = AdoptableTailsState.FAILING
            errors.addAll(result.errors)
        }
        adRepositoryDone = adRepositoryRead
    }
}