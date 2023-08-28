package ru.otus.otuskotlin.adoptabletails.repository.tests

import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAd
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAdId
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAdStatus
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetTemperament
import java.math.BigDecimal
import java.time.Instant

abstract class BaseInitAds(val operation: String) : InitObjects<PetAd> {
    fun createInitTestModel(
        suf: String,
        name: String = "Test Name",
        breed: String = "Test Breed",
        petType: String = "",
        age: BigDecimal = BigDecimal.ZERO,
        temperament: PetTemperament = PetTemperament.NONE,
        size: String = "Test Size",
        description: String = "Test Description",
        petAdStatus: PetAdStatus = PetAdStatus.CREATED
    ) = PetAd(
        id = PetAdId("order-repository-$operation-$suf"),
        name = name,
        breed = breed,
        age = age,
        temperament = temperament,
        size = size,
        description = description,
        petAdStatus = petAdStatus,
        createdAt = Instant.parse("2023-03-03T08:05:57Z")
    )
}