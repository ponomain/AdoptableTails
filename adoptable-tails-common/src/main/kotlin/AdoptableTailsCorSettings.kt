package ru.otus.otuskotlin.adoptabletails.common

import ru.otus.otuskotlin.adoptabletails.lib.log.common.LoggerProvider

data class AdoptableTailsCorSettings(
    val loggerProvider: LoggerProvider = LoggerProvider(),
) {
    companion object {
        val NONE = AdoptableTailsCorSettings
    }
}