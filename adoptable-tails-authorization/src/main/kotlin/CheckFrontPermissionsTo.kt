package ru.otus.otuskotlin.adoptabletails.authorization

import ru.otus.otuskotlin.adoptabletails.common.permissions.CommonAdPermissionClient
import ru.otus.otuskotlin.adoptabletails.common.permissions.CommonPrincipalRelations
import ru.otus.otuskotlin.adoptabletails.common.permissions.CommonUserPermissions

fun resolveFrontPermissions(
    permissions: Iterable<CommonUserPermissions>,
    relations: Iterable<CommonPrincipalRelations>,
) = mutableSetOf<CommonAdPermissionClient>()
    .apply {
        for (permission in permissions) {
            for (relation in relations) {
                accessTable[permission]?.get(relation)?.let { this@apply.add(it) }
            }
        }
    }
    .toSet()

private val accessTable = mapOf(
    // Create
    CommonUserPermissions.CREATE_OWN to mapOf(
        CommonPrincipalRelations.OWN to CommonAdPermissionClient.CREATE
    ),

    // Read
    CommonUserPermissions.READ_OWN to mapOf(
        CommonPrincipalRelations.OWN to CommonAdPermissionClient.READ
    ),
    CommonUserPermissions.READ_ALL to mapOf(
        CommonPrincipalRelations.NONE to CommonAdPermissionClient.READ
    ),

    // Delete
    CommonUserPermissions.DELETE_OWN to mapOf(
        CommonPrincipalRelations.OWN to CommonAdPermissionClient.DELETE
    ),
)