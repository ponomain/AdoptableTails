package ru.otus.otuskotlin.adoptabletails.mappers.exception

import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsCommand

class UnsupportedCommandException(cmd: AdoptableTailsCommand)
    : Throwable("Wrong command $cmd at mapping toTransport stage")