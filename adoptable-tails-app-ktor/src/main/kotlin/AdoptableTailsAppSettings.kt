package ru.otus.otuskotlin.adoptabletails.app.ktor

import ru.otus.otuskotlin.adoptabletails.biz.AdoptableTailsProcessor
import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsCorSettings

data class AdoptableTailsAppSettings(
    val corSettings: AdoptableTailsCorSettings,
    val adoptableTailsProcessor: AdoptableTailsProcessor = AdoptableTailsProcessor()
)