package ru.otus.otuskotlin.adoptabletails.biz

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsContext
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsCommand
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsState
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsWorkMode
import ru.otus.otuskotlin.adoptabletails.common.stubs.PetAdStubs
import ru.otus.otuskotlin.adoptabletails.stubs.PetAdStub

class StubMeetingTest : FunSpec({

    test("STUB test: pet ad create success") {
        val ctx = AdoptableTailsContext().apply {
            command = AdoptableTailsCommand.CREATE
            stub = PetAdStubs.SUCCESS
            workMode = AdoptableTailsWorkMode.STUB
            petAdRequest = PetAdStub.getPetAd()
        }
        val stub = PetAdStub.getPetAd().copy()
        AdoptableTailsProcessor().exec(ctx)
        ctx.state shouldBe AdoptableTailsState.FINISHING
        ctx.petAdResponse shouldBe stub
    }

    test("STUB test: pet ad create bad age") {
        val ctx = AdoptableTailsContext().apply {
            command = AdoptableTailsCommand.CREATE
            stub = PetAdStubs.BAD_AGE
            workMode = AdoptableTailsWorkMode.STUB
        }
        AdoptableTailsProcessor().exec(ctx)
        ctx.state shouldBe AdoptableTailsState.FAILING
        ctx.errors[0].message shouldBe "Wrong age field"
    }

    test("STUB test: pet ad create bad breed") {
        val ctx = AdoptableTailsContext().apply {
            command = AdoptableTailsCommand.CREATE
            stub = PetAdStubs.BAD_BREED
            workMode = AdoptableTailsWorkMode.STUB
        }
        AdoptableTailsProcessor().exec(ctx)
        ctx.state shouldBe AdoptableTailsState.FAILING
        ctx.errors[0].message shouldBe "Wrong breed field"
    }

    test("STUB test: pet ad create bad name") {
        val ctx = AdoptableTailsContext().apply {
            command = AdoptableTailsCommand.CREATE
            stub = PetAdStubs.BAD_NAME
            workMode = AdoptableTailsWorkMode.STUB
        }
        AdoptableTailsProcessor().exec(ctx)
        ctx.state shouldBe AdoptableTailsState.FAILING
        ctx.errors[0].message shouldBe "Wrong name field"
    }

    test("STUB test: pet ad read success") {
        val ctx = AdoptableTailsContext().apply {
            command = AdoptableTailsCommand.READ
            stub = PetAdStubs.SUCCESS
            workMode = AdoptableTailsWorkMode.STUB
            petAdRequest = PetAdStub.getPetAd()
        }
        AdoptableTailsProcessor().exec(ctx)
        ctx.state shouldBe AdoptableTailsState.FINISHING
        ctx.petAdResponse shouldBe PetAdStub.getPetAd()
    }

    test("STUB test: pet ad read bad id") {
        val ctx = AdoptableTailsContext().apply {
            command = AdoptableTailsCommand.READ
            stub = PetAdStubs.BAD_ID
            workMode = AdoptableTailsWorkMode.STUB
        }
        AdoptableTailsProcessor().exec(ctx)
        ctx.state shouldBe AdoptableTailsState.FAILING
        ctx.errors[0].message shouldBe "Wrong id field"
    }

    test("STUB test: pet ad update success") {
        val ctx = AdoptableTailsContext().apply {
            command = AdoptableTailsCommand.UPDATE
            stub = PetAdStubs.SUCCESS
            workMode = AdoptableTailsWorkMode.STUB
            petAdRequest = PetAdStub.getPetAd()
        }
        AdoptableTailsProcessor().exec(ctx)
        ctx.state shouldBe AdoptableTailsState.FINISHING
        ctx.petAdResponse shouldBe PetAdStub.getPetAd()
    }

    test("STUB test: pet ad update bad id") {
        val ctx = AdoptableTailsContext().apply {
            command = AdoptableTailsCommand.UPDATE
            stub = PetAdStubs.BAD_ID
            workMode = AdoptableTailsWorkMode.STUB
        }
        AdoptableTailsProcessor().exec(ctx)
        ctx.state shouldBe AdoptableTailsState.FAILING
        ctx.errors[0].message shouldBe "Wrong id field"
    }

    test("STUB test: pet ad update bad name") {
        val ctx = AdoptableTailsContext().apply {
            command = AdoptableTailsCommand.UPDATE
            stub = PetAdStubs.BAD_NAME
            workMode = AdoptableTailsWorkMode.STUB
        }
        AdoptableTailsProcessor().exec(ctx)
        ctx.state shouldBe AdoptableTailsState.FAILING
        ctx.errors[0].message shouldBe "Wrong name field"
    }

    test("STUB test: pet ad update bad breed") {
        val ctx = AdoptableTailsContext().apply {
            command = AdoptableTailsCommand.UPDATE
            stub = PetAdStubs.BAD_BREED
            workMode = AdoptableTailsWorkMode.STUB
        }
        AdoptableTailsProcessor().exec(ctx)
        ctx.state shouldBe AdoptableTailsState.FAILING
        ctx.errors[0].message shouldBe "Wrong breed field"
    }

    test("STUB test: pet ad delete success") {
        val ctx = AdoptableTailsContext().apply {
            command = AdoptableTailsCommand.DELETE
            stub = PetAdStubs.SUCCESS
            workMode = AdoptableTailsWorkMode.STUB
        }
        AdoptableTailsProcessor().exec(ctx)
        ctx.state shouldBe AdoptableTailsState.FINISHING
    }

    test("STUB test: pet ad delete bad id") {
        val ctx = AdoptableTailsContext().apply {
            command = AdoptableTailsCommand.UPDATE
            stub = PetAdStubs.BAD_ID
            workMode = AdoptableTailsWorkMode.STUB
        }
        AdoptableTailsProcessor().exec(ctx)
        ctx.state shouldBe AdoptableTailsState.FAILING
        ctx.errors[0].message shouldBe "Wrong id field"
    }

    test("STUB test: pet ad search success") {
        val ctx = AdoptableTailsContext().apply {
            command = AdoptableTailsCommand.SEARCH
            stub = PetAdStubs.SUCCESS
            workMode = AdoptableTailsWorkMode.STUB
        }
        AdoptableTailsProcessor().exec(ctx)
        ctx.state shouldBe AdoptableTailsState.FINISHING
        ctx.petAdsResponse shouldBe PetAdStub.getPetAds()
    }
})