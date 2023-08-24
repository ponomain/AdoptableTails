package ru.otus.otuskotlin.adoptabletails.repository.cassandra.model

import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAdStatus

enum class AdStatusCassandra {
    CREATED,
    RESERVED,
    ADOPTED
}

fun AdStatusCassandra?.fromTransport() = when(this) {
    null -> PetAdStatus.NONE
    AdStatusCassandra.CREATED -> PetAdStatus.CREATED
    AdStatusCassandra.RESERVED -> PetAdStatus.RESERVED
    AdStatusCassandra.ADOPTED -> PetAdStatus.ADOPTED
}

fun PetAdStatus.toTransport() = when(this) {
    PetAdStatus.NONE -> null
    PetAdStatus.CREATED -> AdStatusCassandra.CREATED
    PetAdStatus.RESERVED -> AdStatusCassandra.RESERVED
    PetAdStatus.ADOPTED -> AdStatusCassandra.ADOPTED
}

