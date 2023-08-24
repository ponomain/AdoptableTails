package ru.otus.otuskotlin.adoptabletails.repository.cassandra.model

import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetTemperament

enum class PetTemperamentCassandra {
    SANGUINE,
    CHOLERIC,
    PHLEGMATIC,
    MELANCHOLIC
}

fun PetTemperamentCassandra?.fromTransport() = when (this) {
    null -> PetTemperament.NONE
    PetTemperamentCassandra.CHOLERIC -> PetTemperament.CHOLERIC
    PetTemperamentCassandra.MELANCHOLIC -> PetTemperament.MELANCHOLIC
    PetTemperamentCassandra.PHLEGMATIC -> PetTemperament.PHLEGMATIC
    PetTemperamentCassandra.SANGUINE -> PetTemperament.SANGUINE
}

fun PetTemperament.toTransport() = when (this) {
    PetTemperament.NONE -> null
    PetTemperament.SANGUINE -> PetTemperamentCassandra.SANGUINE
    PetTemperament.MELANCHOLIC -> PetTemperamentCassandra.MELANCHOLIC
    PetTemperament.PHLEGMATIC -> PetTemperamentCassandra.PHLEGMATIC
    PetTemperament.CHOLERIC -> PetTemperamentCassandra.CHOLERIC
}
