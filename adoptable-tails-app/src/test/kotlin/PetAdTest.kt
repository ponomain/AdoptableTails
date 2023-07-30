package ru.otus.otuskotlin.adoptabletails

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class PetAdTest : FunSpec({

    test("test status of new created ad") {
        val newPetAdStatus = PetAd().status
        newPetAdStatus shouldBe "CREATED"
    }

    test("test breed and name of created ad") {
        val newPetAd = PetAd().apply {
            breed = "test"
            name = "test"
        }
        newPetAd.name shouldBe "test"
        newPetAd.status shouldBe "test"
    }
})