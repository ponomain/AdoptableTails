package ru.otus.otuskotlin.adoptabletails.mappers.exception

import ru.otus.otuskotlin.adoptabletails.common.models.PetAdCommand

class UnsupportedCommandException(cmd: PetAdCommand)
    : Throwable("Wrong command $cmd at mapping toTransport stage")