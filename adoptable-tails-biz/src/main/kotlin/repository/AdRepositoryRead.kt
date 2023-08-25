package ru.otus.otuskotlin.adoptabletails.biz.repository

import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsContext
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsState
import ru.otus.otuskotlin.adoptabletails.common.repository.DbAdIdRequest
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.CorChainDsl
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.handle
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.on
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.worker

fun CorChainDsl<AdoptableTailsContext>.repositoryRead(title: String) = worker {
    this.title = title
    description = "Чтение объекта из БД"
    on { state == AdoptableTailsState.RUNNING }
    handle {
        val request = DbAdIdRequest(petAdValidated)
        val result = adRepository.readAd(request)
        val resultOrder = result.data
        if (result.isSuccess && resultOrder != null) {
            adRepositoryRead = resultOrder
        } else {
            state = AdoptableTailsState.FAILING
            errors.addAll(result.errors)
        }
    }
}