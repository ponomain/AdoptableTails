package ru.otus.otuskotlin.adoptabletails.app.ktor

import ru.otus.otuskotlin.adoptabletails.app.ktor.configs.KtorAuthConfig
import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsCorSettings
import ru.otus.otuskotlin.adoptabletails.common.repository.AdRepository
import ru.otus.otuskotlin.adoptabletails.repository.stubs.AdRepositoryStub

fun testSettings(repo: AdRepository? = null) = AdoptableTailsAppSettings(
    corSettings = AdoptableTailsCorSettings(
        repositoryStub = AdRepositoryStub(),
        repositoryTest = repo ?: AdRepositoryStub(),
        repositoryProd = repo ?: AdRepositoryStub(),

        ),
    authorization = KtorAuthConfig.TEST
)