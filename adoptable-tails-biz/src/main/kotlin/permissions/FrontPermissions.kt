package ru.otus.otuskotlin.adoptabletails.biz.permissions

import ru.otus.otuskotlin.adoptabletails.authorization.resolveFrontPermissions
import ru.otus.otuskotlin.adoptabletails.authorization.resolveRelationsTo
import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsContext
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsState
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.CorChainDsl
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.handle
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.on
import ru.otus.otuskotlin.adoptabletails.lib.cor.dsl.handlers.worker

fun CorChainDsl<AdoptableTailsContext>.frontPermissions(title: String) = worker {
    this.title = title
    description = "Calculating user permissions for the frontend"

    on { state == AdoptableTailsState.RUNNING }

    handle {
        adRepositoryDone.permissionsClient.addAll(
            resolveFrontPermissions(
                permissionsChain,
                // Recomputing relationships, as they might have changed during the execution of the operation.
                adRepositoryDone.resolveRelationsTo(principal)
            )
        )

        for (order in adsRepositoryDone) {
            order.permissionsClient.addAll(
                resolveFrontPermissions(
                    permissionsChain,
                    order.resolveRelationsTo(principal)
                )
            )
        }
    }
}