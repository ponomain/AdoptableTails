package ru.otus.otuskotlin.adoptabletails.app.ktor.plugins

import io.ktor.server.application.*
import ru.otus.otuskotlin.adoptabletails.app.ktor.AdoptableTailsAppSettings
import ru.otus.otuskotlin.adoptabletails.app.ktor.configs.AdDbType
import ru.otus.otuskotlin.adoptabletails.biz.AdoptableTailsProcessor
import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsCorSettings
import ru.otus.otuskotlin.adoptabletails.lib.log.common.LogWrapper
import ru.otus.otuskotlin.adoptabletails.lib.log.common.LoggerProvider

fun Application.initAppSettings(): AdoptableTailsAppSettings {
    return AdoptableTailsAppSettings(
        corSettings = AdoptableTailsCorSettings(
            loggerProvider = LoggerProvider { LogWrapper.DEFAULT },
            repositoryTest = getDatabaseConf(AdDbType.TEST),
            repositoryProd = getDatabaseConf(AdDbType.PROD)
        ),
        adoptableTailsProcessor = AdoptableTailsProcessor()
    )
}