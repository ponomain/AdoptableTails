package ru.otus.otuskotlin.adoptabletails.biz.workers.stubs

import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsContext
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsState
import ru.otus.otuskotlin.adoptabletails.common.stubs.PetAdStubs
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.CorChainDsl
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.handle
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.on
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.worker
import ru.otus.otuskotlin.adoptabletails.stubs.PetAdStub

fun CorChainDsl<AdoptableTailsContext>.stubDeleteSuccess(title: String) = worker {
    this.title = title
    on { stub == PetAdStubs.SUCCESS && state == AdoptableTailsState.RUNNING }
    handle {
        state = AdoptableTailsState.FINISHING
        val stub = PetAdStub.getPetAd()
        petAdResponse = stub
    }
}