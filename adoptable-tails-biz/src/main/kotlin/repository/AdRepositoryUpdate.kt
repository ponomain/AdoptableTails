package ru.otus.otuskotlin.adoptabletails.biz.repository

import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsContext
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsState
import ru.otus.otuskotlin.adoptabletails.common.models.helpers.fail
import ru.otus.otuskotlin.adoptabletails.common.repository.DbAdUpdateRequest
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.CorChainDsl
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.handle
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.on
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.worker

fun CorChainDsl<AdoptableTailsContext>.repositoryUpdate(title: String) = worker {
    this.title = "Updating object"
    on { state == AdoptableTailsState.RUNNING }
    handle {
        val request = DbAdUpdateRequest(
            id = petAdValidated.id,
            status = petAdValidated.petAdStatus
        )
        val result = adRepository.updateAd(request)
        val resultData = result.data
        if (result.isSuccess && resultData != null) {
            adRepositoryDone = resultData
        } else {
            state = AdoptableTailsState.FAILING
            errors.addAll(result.errors)
        }
    }
}