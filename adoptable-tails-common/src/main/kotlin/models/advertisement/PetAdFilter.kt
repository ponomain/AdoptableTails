package ru.otus.otuskotlin.adoptabletails.common.models.advertisement

data class PetAdFilter(
    val breed: String = "",
    val type: PetType = PetType.NONE,
    val temperament: PetTemperament = PetTemperament.NONE
)