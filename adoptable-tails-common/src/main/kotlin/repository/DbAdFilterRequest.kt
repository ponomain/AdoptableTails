package ru.otus.otuskotlin.adoptabletails.common.repository

import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetTemperament

data class DbAdFilterRequest(
    val breed: String = "",
    val temperament: PetTemperament = PetTemperament.NONE
)