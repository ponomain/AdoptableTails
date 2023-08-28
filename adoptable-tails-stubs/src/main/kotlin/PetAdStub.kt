package ru.otus.otuskotlin.adoptabletails.stubs

import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAd
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAdId
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAdStatus
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetTemperament
import java.math.BigDecimal

object PetAdStub {

    private val petAdCaseOne = PetAd(
        id = PetAdId("1234567890"),
        name = "Rice",
        age = BigDecimal.valueOf(2.3),
        description = "Good and king cat",
        breed = "street",
        temperament = PetTemperament.PHLEGMATIC,
        size = "medium"
    )

    private val petAdCaseTwo = PetAd(
        id = PetAdId("000000000000"),
        name = "Leaf",
        description = "Very energetic and funny dog ",
        breed = "Golden retriever",
        age = BigDecimal.valueOf(5.6),
        temperament = PetTemperament.SANGUINE,
        size = "above average",
        petAdStatus = PetAdStatus.ADOPTED
    )

    fun getPetAd() = petAdCaseTwo

    fun getPetAds() = mutableListOf(petAdCaseOne, petAdCaseTwo)


}