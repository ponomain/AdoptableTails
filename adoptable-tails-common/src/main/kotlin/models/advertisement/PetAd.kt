package ru.otus.otuskotlin.adoptabletails.common.models.advertisement

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ru.otus.otuskotlin.adoptabletails.common.NONE
import java.math.BigDecimal

data class PetAd(
    var id: PetAdId = PetAdId.NONE,
    var name: String = "",
    var breed: String = "",
    var petType: PetType = PetType.NONE,
    var age: BigDecimal = BigDecimal.ZERO,
    var temperament: PetTemperament = PetTemperament.NONE,
    var size: String = "",
    var description: String = "",
    var createdAt: Instant = Clock.System.now(),
    var updatedAt: Instant = Instant.NONE,
    var petAdStatus: PetAdStatus = PetAdStatus.CREATED
)