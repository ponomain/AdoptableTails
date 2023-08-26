package ru.otus.otuskotlin.adoptabletails.biz.permissions

import ru.otus.otuskotlin.adoptabletails.authorization.checkPermitted
import ru.otus.otuskotlin.adoptabletails.authorization.resolveRelationsTo
import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsContext
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsError
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsState
import ru.otus.otuskotlin.adoptabletails.common.models.helpers.fail
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.CorChainDsl
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.chain
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.handle
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.on
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.worker

fun CorChainDsl<AdoptableTailsContext>.accessValidation(title: String) = chain {
    this.title = title
    description = "Calculating permissions based on a principal's group and an access rights table"
    on { state == AdoptableTailsState.RUNNING }
    worker("Calculation of the ad's relation to the principal.") {
        adRepositoryRead.principalRelations = adRepositoryRead.resolveRelationsTo(principal)
    }
    worker("Calculating access to ads") {
        permitted = checkPermitted(command, adRepositoryRead.principalRelations, permissionsChain)
    }
    worker {
        this.title = "Validation of access rights."
        description = "Checking the presence of permissions to perform an operation"
        on { !permitted }
        handle {
            fail(AdoptableTailsError(message = "User is not allowed to perform this operation"))
        }
    }
}