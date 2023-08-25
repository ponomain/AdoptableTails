package ru.otus.otuskotlin.adoptabletails.biz.workers

import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsContext
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsWorkMode
import ru.otus.otuskotlin.adoptabletails.common.models.helpers.errorAdministration
import ru.otus.otuskotlin.adoptabletails.common.models.helpers.fail
import ru.otus.otuskotlin.adoptabletails.common.repository.AdRepository
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.CorChainDsl
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.handle
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.worker

fun CorChainDsl<AdoptableTailsContext>.initRepository(title: String) = worker {
    this.title = title
    description = "Calculating the main working repository based on the requested operational mode.".trimIndent()
    handle {
        adRepository = when (workMode) {
            AdoptableTailsWorkMode.TEST -> settings.repositoryTest
            AdoptableTailsWorkMode.STUB -> settings.repositoryStub
            else -> settings.repositoryProd
        }
        if (workMode != AdoptableTailsWorkMode.STUB && adRepository == AdRepository.NONE) fail(
            errorAdministration(
                field = "repo",
                violationCode = "dbNotConfigured",
                description = "The database is unconfigured for chosen work mode ($workMode). Please, contact the administrator staff"
            )
        )
    }
}