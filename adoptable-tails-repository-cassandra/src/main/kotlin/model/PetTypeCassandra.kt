package ru.otus.otuskotlin.adoptabletails.repository.cassandra.model

import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetType

enum class PetTypeCassandra {
    DOG,
    CAT
}

fun PetTypeCassandra?.fromTransport() = when(this) {
    null -> PetType.NONE
    PetTypeCassandra.CAT -> PetType.CAT
    PetTypeCassandra.DOG -> PetType.DOG
}

fun PetType.toTransport() = when(this) {
    PetType.NONE -> null
    PetType.CAT -> PetTypeCassandra.CAT
    PetType.DOG -> PetTypeCassandra.DOG
}
