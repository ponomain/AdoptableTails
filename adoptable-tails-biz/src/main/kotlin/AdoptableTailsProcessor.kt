package ru.otus.otuskotlin.adoptabletails.biz

import ru.otus.otuskotlin.adoptabletails.biz.ad.operation
import ru.otus.otuskotlin.adoptabletails.biz.validation.finishPetAdValidation
import ru.otus.otuskotlin.adoptabletails.biz.validation.validateIdNotEmpty
import ru.otus.otuskotlin.adoptabletails.biz.validation.validateIdProperFormat
import ru.otus.otuskotlin.adoptabletails.biz.validation.validatePetAdAge
import ru.otus.otuskotlin.adoptabletails.biz.validation.validatePetAdBreed
import ru.otus.otuskotlin.adoptabletails.biz.validation.validatePetAdName
import ru.otus.otuskotlin.adoptabletails.biz.validation.validation
import ru.otus.otuskotlin.adoptabletails.biz.workers.initStatus
import ru.otus.otuskotlin.adoptabletails.biz.workers.stubs.stubCreateSuccess
import ru.otus.otuskotlin.adoptabletails.biz.workers.stubs.stubDbError
import ru.otus.otuskotlin.adoptabletails.biz.workers.stubs.stubDeleteSuccess
import ru.otus.otuskotlin.adoptabletails.biz.workers.stubs.stubNoCase
import ru.otus.otuskotlin.adoptabletails.biz.workers.stubs.stubReadSuccess
import ru.otus.otuskotlin.adoptabletails.biz.workers.stubs.stubSearchSuccess
import ru.otus.otuskotlin.adoptabletails.biz.workers.stubs.stubUpdateSuccess
import ru.otus.otuskotlin.adoptabletails.biz.workers.stubs.stubValidationBadAge
import ru.otus.otuskotlin.adoptabletails.biz.workers.stubs.stubValidationBadBreed
import ru.otus.otuskotlin.adoptabletails.biz.workers.stubs.stubValidationBadId
import ru.otus.otuskotlin.adoptabletails.biz.workers.stubs.stubValidationBadName
import ru.otus.otuskotlin.adoptabletails.biz.workers.stubs.stubs
import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsContext
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsCommand
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAdId
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.rootChain
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.worker

class AdoptableTailsProcessor {
    suspend fun exec(ctx: AdoptableTailsContext) = BusinessChain.exec(ctx)

    companion object {
        private val BusinessChain = rootChain<AdoptableTailsContext> {
            initStatus("Status initialization")

            operation("Create product group", AdoptableTailsCommand.CREATE) {
                stubs("Stub processing") {
                    stubCreateSuccess("Simulation of successful processing")
                    stubValidationBadName("Simulation a name validation error")
                    stubValidationBadBreed("Simulation a breed validation error")
                    stubValidationBadAge("Simulation an age validation error")
                    stubDbError("Simulation a db work error")
                    stubNoCase("Error: requested stub not permit")
                }
                validation {
                    worker("Copying the fields to downWorkValidating") { petAdValidating = petAdRequest.copy() }
                    worker("Cleaning id") { petAdValidating.id = PetAdId.NONE }

                    worker("Cleaning name") { petAdValidating.name = petAdValidating.name.trim() }
                    worker("Cleaning breed") {
                        petAdValidating.breed = petAdValidating.breed.trim()
                    }

                    validatePetAdBreed("Check breed")
                    validatePetAdName("Check name")
                    validatePetAdAge("Check age")

                    finishPetAdValidation("Completion of checks")
                }
            }

            operation("Receive product group", AdoptableTailsCommand.READ) {
                stubs("Stub processing") {
                    stubReadSuccess("Simulation of successful processing")
                    stubValidationBadId("Simulation an id validation error")
                    stubDbError("Simulation a db work error")
                    stubNoCase("Error: requested stub does not permit")
                }

                validation {
                    worker("Copying the fields to petAdValidating") { petAdValidating = petAdRequest.copy() }
                    worker("Cleaning id") {
                        petAdValidating.id = PetAdId(petAdValidating.id.asString().trim())
                    }

                    validateIdNotEmpty("Check id is not empty")
                    validateIdProperFormat("Check id format")

                    finishPetAdValidation("Completion of checks")
                }
            }

            operation("Change product group", AdoptableTailsCommand.UPDATE) {
                stubs("Stub processing") {
                    stubUpdateSuccess("Simulation of successful processing")
                    stubValidationBadId("Simulation an id validation error")
                    stubValidationBadBreed("Simulation a breed validation error")
                    stubValidationBadName("Simulation a name validation error")
                    stubValidationBadAge("Simulation an age validation error")
                    stubDbError("Simulation a db work error")
                    stubNoCase("Error: requested stub not permit")
                }

                validation {
                    worker("Copying the fields to downWorkValidating") { petAdValidating = petAdRequest.copy() }
                    worker("Cleaning id") {
                        petAdValidating.id = PetAdId(petAdValidating.id.asString().trim())
                    }
                    worker("Cleaning name") { petAdValidating.name = petAdValidating.name.trim() }
                    worker("Cleaning breed") {
                        petAdValidating.breed = petAdValidating.breed.trim()
                    }

                    validateIdNotEmpty("Check id is not empty")
                    validateIdProperFormat("Check id format")
                    validatePetAdAge("Check age")
                    validatePetAdBreed("Check breed")
                    validatePetAdName("Check name")

                    finishPetAdValidation("Completion of checks")
                }
            }

            operation("Delete product group", AdoptableTailsCommand.DELETE) {
                stubs("Stub processing") {
                    stubDeleteSuccess("Simulation of successful processing")
                    stubValidationBadId("Simulation an id validation error")
                    stubDbError("Simulation a db work error")
                    stubNoCase("Error: requested stub does not permit")
                }

                validation {
                    worker("Copying the fields to downWorkValidating") { petAdValidating = petAdRequest.copy() }
                    worker("Cleaning id") {
                        petAdValidating.id = PetAdId(petAdValidating.id.asString().trim())
                    }

                    validateIdNotEmpty("Check id is not empty")
                    validateIdProperFormat("Check id format")

                    finishPetAdValidation("Completion of checks")
                }
            }

            operation("Search product group", AdoptableTailsCommand.SEARCH) {
                stubs("Stub processing") {
                    stubSearchSuccess("Simulation of successful processing")
                    stubValidationBadId("Simulation an id validation error")
                    stubDbError("Simulation a db work error")
                    stubNoCase("Error: requested stub does not permit")
                }

                validation {
                    worker("Copying the fields to downWorkValidating") { petAdValidating = petAdRequest.copy() }

                    finishPetAdValidation("Completion of checks")
                }
            }
        }.build()
    }
}