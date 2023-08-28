package ru.otus.otuskotlin.adoptabletails.common.repository

import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAdId
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAdStatus
import java.util.*

class DbAdUpdateRequest(
    val id: PetAdId,
    val status: PetAdStatus
)