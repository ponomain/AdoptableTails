package ru.otus.otuskotlin.adoptabletails.common.models.advertisement

data class PetAdFilter(
    val breed: String = "",
    val type: String = "",
    val temperament: PetTemperament = PetTemperament.NONE
)