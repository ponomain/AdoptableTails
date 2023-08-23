package ru.otus.otuskotlin.adoptabletails.app.ktor.plugins

import ru.otus.otuskotlin.adoptabletails.app.ktor.AdoptableTailsAppSettings
import ru.otus.otuskotlin.adoptabletails.biz.AdoptableTailsProcessor
import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsCorSettings
import ru.otus.otuskotlin.adoptabletails.lib.log.common.LogWrapper
import ru.otus.otuskotlin.adoptabletails.lib.log.common.LoggerProvider

fun initAppSettings(): AdoptableTailsAppSettings {
    return AdoptableTailsAppSettings(
        corSettings = AdoptableTailsCorSettings(loggerProvider = LoggerProvider { LogWrapper.DEFAULT }),
        adoptableTailsProcessor = AdoptableTailsProcessor()
    )
}