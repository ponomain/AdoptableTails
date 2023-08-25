package ru.otus.otuskotlin.adoptabletails.biz.workers

import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsContext
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsState
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsWorkMode
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.CorChainDsl
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.handle
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.on
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.worker

fun CorChainDsl<AdoptableTailsContext>.prepareResult(title: String) = worker {
    this.title = title
    description = "Preparing data before response"
    on { workMode != AdoptableTailsWorkMode.STUB }
    handle {
        petAdResponse = adRepositoryDone
        petAdsResponse = adsRepositoryDone
        state = when (val st = state) {
            AdoptableTailsState.RUNNING -> AdoptableTailsState.FINISHING
            else -> st
        }
    }
}