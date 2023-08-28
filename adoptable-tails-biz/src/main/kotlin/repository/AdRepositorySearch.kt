package ru.otus.otuskotlin.adoptabletails.biz.repository

import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsContext
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsState
import ru.otus.otuskotlin.adoptabletails.common.repository.DbAdFilterRequest
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.CorChainDsl
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.handle
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.on
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.worker

fun CorChainDsl<AdoptableTailsContext>.repositorySearch(title: String) = worker {
    this.title = title
    description = "Searching objects in the DB with filter"
    on { state == AdoptableTailsState.RUNNING }
    handle {
        val request = DbAdFilterRequest(
            breed = petAdFilterValidated.breed,
            temperament = petAdFilterValidated.temperament,
        )
        val result = adRepository.searchAd(request)
        val resultAds = result.data
        if (result.isSuccess && resultAds != null) {
            adsRepositoryDone = resultAds.toMutableList()
        } else {
            state = AdoptableTailsState.FAILING
            errors.addAll(result.errors)
        }
    }
}