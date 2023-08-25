package ru.otus.otuskotlin.adoptabletails.common.repository

import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsError

interface DbResponse<T> {
    val data: T?
    val isSuccess: Boolean
    val errors: List<AdoptableTailsError>
}