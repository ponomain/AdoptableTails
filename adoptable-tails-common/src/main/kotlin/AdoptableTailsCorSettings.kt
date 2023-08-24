package ru.otus.otuskotlin.adoptabletails.common

import ru.otus.otuskotlin.adoptabletails.common.repository.AdRepository
import ru.otus.otuskotlin.adoptabletails.lib.log.common.LoggerProvider

data class AdoptableTailsCorSettings(
    val loggerProvider: LoggerProvider = LoggerProvider(),
    var repositoryStub: AdRepository = AdRepository.NONE,
    var repositoryTest: AdRepository = AdRepository.NONE,
    var repositoryProd: AdRepository = AdRepository.NONE
) {
    companion object {
        val NONE = AdoptableTailsCorSettings
    }
}