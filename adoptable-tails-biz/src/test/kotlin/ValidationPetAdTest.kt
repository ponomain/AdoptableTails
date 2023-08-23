package ru.otus.otuskotlin.adoptabletails.biz

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsContext
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsCommand
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsState
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsWorkMode
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAdId
import ru.otus.otuskotlin.adoptabletails.stubs.PetAdStub
import java.math.BigDecimal

class ValidationMeetingTest : FunSpec({

    test("Validation test: create pet ad, bad name") {
        val ctx = AdoptableTailsContext().apply {
            command = AdoptableTailsCommand.CREATE
            workMode = AdoptableTailsWorkMode.PROD
            petAdRequest = PetAdStub.getPetAd().copy().apply { (0..30).forEach { name = name.plus("a") } }
        }
        AdoptableTailsProcessor().exec(ctx)
        ctx.state shouldBe AdoptableTailsState.FAILING
        ctx.errors[0].message shouldBe "Wrong name, length must not exceed 20 characters"
    }

    test("Validation test: create pet ad, bad breed") {
        val ctx = AdoptableTailsContext().apply {
            command = AdoptableTailsCommand.CREATE
            workMode = AdoptableTailsWorkMode.PROD
            petAdRequest = PetAdStub.getPetAd().copy().apply { breed = "" }
        }
        AdoptableTailsProcessor().exec(ctx)
        ctx.state shouldBe AdoptableTailsState.FAILING
        ctx.errors[0].message shouldBe "Breed cannot be empty"
    }

    test("Validation test: create pet ad,  bad age") {
        val ctx = AdoptableTailsContext().apply {
            command = AdoptableTailsCommand.CREATE
            workMode = AdoptableTailsWorkMode.PROD
            petAdRequest = PetAdStub.getPetAd().copy().apply { age = BigDecimal(-20) }
        }
        AdoptableTailsProcessor().exec(ctx)
        ctx.state shouldBe AdoptableTailsState.FAILING
        ctx.errors[0].message shouldBe "Wrong age. Age cannot be less than 0"
    }

    test("Validation test: read pet ad, bad id") {
        val ctx = AdoptableTailsContext().apply {
            command = AdoptableTailsCommand.READ
            workMode = AdoptableTailsWorkMode.PROD
            petAdRequest = PetAdStub.getPetAd().copy().apply { id = PetAdId.NONE }
        }
        AdoptableTailsProcessor().exec(ctx)
        ctx.state shouldBe AdoptableTailsState.FAILING
        ctx.errors[0].message shouldBe "Validation error for field id: field must not be empty"
    }

    test("Validation test: update pet ad, bad id") {
        val ctx = AdoptableTailsContext().apply {
            command = AdoptableTailsCommand.UPDATE
            workMode = AdoptableTailsWorkMode.PROD
            petAdRequest = PetAdStub.getPetAd().copy().apply { id = PetAdId.NONE }
        }
        AdoptableTailsProcessor().exec(ctx)
        ctx.state shouldBe AdoptableTailsState.FAILING
        ctx.errors[0].message shouldBe "Validation error for field id: field must not be empty"
    }

    test("Validation test: update pet ad, bad name") {
        val ctx = AdoptableTailsContext().apply {
            command = AdoptableTailsCommand.UPDATE
            workMode = AdoptableTailsWorkMode.PROD
            petAdRequest = PetAdStub.getPetAd().copy().apply { (0..30).forEach { name = name.plus("a") } }
        }
        AdoptableTailsProcessor().exec(ctx)
        ctx.state shouldBe AdoptableTailsState.FAILING
        ctx.errors[0].message shouldBe "Wrong name, length must not exceed 20 characters"
    }

    test("Validation test: update pet ad, bad age") {
        val ctx = AdoptableTailsContext().apply {
            command = AdoptableTailsCommand.UPDATE
            workMode = AdoptableTailsWorkMode.PROD
            petAdRequest = PetAdStub.getPetAd().copy().apply { age = BigDecimal(-20) }
        }
        AdoptableTailsProcessor().exec(ctx)
        ctx.state shouldBe AdoptableTailsState.FAILING
        ctx.errors[0].message shouldBe "Wrong age. Age cannot be less than 0"
    }

    test("Validation test: update pet ad, bad breed") {
        val ctx = AdoptableTailsContext().apply {
            command = AdoptableTailsCommand.UPDATE
            workMode = AdoptableTailsWorkMode.PROD
            petAdRequest = PetAdStub.getPetAd().copy().apply { breed = "" }
        }
        AdoptableTailsProcessor().exec(ctx)
        ctx.state shouldBe AdoptableTailsState.FAILING
        ctx.errors[0].message shouldBe "Breed cannot be empty"
    }

    test("Validation test: delete pet ad, bad id") {
        val ctx = AdoptableTailsContext().apply {
            command = AdoptableTailsCommand.DELETE
            workMode = AdoptableTailsWorkMode.PROD
            petAdRequest = PetAdStub.getPetAd().copy().apply { id = PetAdId.NONE }
        }
        AdoptableTailsProcessor().exec(ctx)
        ctx.state shouldBe AdoptableTailsState.FAILING
        ctx.errors[0].message shouldBe "Validation error for field id: field must not be empty"
    }
})