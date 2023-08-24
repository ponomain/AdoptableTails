package ru.otus.otuskotlin.adoptabletails.common.repository

import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAd
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAdId

data class DbAdIdRequest(
    val id: PetAdId
) {
    constructor(order: PetAd) : this(order.id)
}