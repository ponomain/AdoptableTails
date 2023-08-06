package ru.otus.otuskotlin.adoptabletails.common.models

data class PetAdError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val exception: Throwable? = null
)