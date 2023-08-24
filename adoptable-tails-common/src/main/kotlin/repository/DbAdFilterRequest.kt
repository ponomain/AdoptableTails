package ru.otus.otuskotlin.adoptabletails.common.repository

import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetTemperament
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetType

data class DbAdFilterRequest(
    val breed: String = "",
    val type: PetType = PetType.NONE,
    val temperament: PetTemperament = PetTemperament.NONE
)