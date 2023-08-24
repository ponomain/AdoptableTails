package ru.otus.otuskotlin.adoptabletails.authorization

import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsCommand
import ru.otus.otuskotlin.adoptabletails.common.permissions.CommonPrincipalRelations
import ru.otus.otuskotlin.adoptabletails.common.permissions.CommonUserPermissions

fun checkPermitted(
    command: AdoptableTailsCommand,
    relations: Iterable<CommonPrincipalRelations>,
    permissions: Iterable<CommonUserPermissions>,
) =
    relations.asSequence().flatMap { relation ->
        permissions.map { permission ->
            AccessTableConditions(
                command = command,
                permission = permission,
                relation = relation,
            )
        }
    }.any {
        accessTable[it] != null
    }

private data class AccessTableConditions(
    val command: AdoptableTailsCommand,
    val permission: CommonUserPermissions,
    val relation: CommonPrincipalRelations
)

private val accessTable = mapOf(
    // Create
    AccessTableConditions(
        command = AdoptableTailsCommand.CREATE,
        permission = CommonUserPermissions.CREATE_OWN,
        relation = CommonPrincipalRelations.OWN,
    ) to true,

    // Read
    AccessTableConditions(
        command = AdoptableTailsCommand.READ,
        permission = CommonUserPermissions.READ_OWN,
        relation = CommonPrincipalRelations.OWN,
    ) to true,
    AccessTableConditions(
        command = AdoptableTailsCommand.READ,
        permission = CommonUserPermissions.READ_ALL,
        relation = CommonPrincipalRelations.NONE,
    ) to true,

    // Delete
    AccessTableConditions(
        command = AdoptableTailsCommand.DELETE,
        permission = CommonUserPermissions.DELETE_OWN,
        relation = CommonPrincipalRelations.OWN,
    ) to true,

    // Search
    AccessTableConditions(
        command = AdoptableTailsCommand.SEARCH,
        permission = CommonUserPermissions.READ_ALL,
        relation = CommonPrincipalRelations.NONE,
    ) to true,
)