package ru.otus.otuskotlin.adoptabletails.authorization

import ru.otus.otuskotlin.adoptabletails.common.permissions.CommonUserGroups
import ru.otus.otuskotlin.adoptabletails.common.permissions.CommonUserPermissions

fun resolveChainPermissions(
    groups: Iterable<CommonUserGroups>,
) = mutableSetOf<CommonUserPermissions>()
    .apply {
        addAll(groups.flatMap { groupPermissionsAdmits[it] ?: emptySet() })
        removeAll(groups.flatMap { groupPermissionsDenys[it] ?: emptySet() }.toSet())
    }
    .toSet()

private val groupPermissionsAdmits = mapOf(
    CommonUserGroups.USER to setOf(
        CommonUserPermissions.READ_OWN,
        CommonUserPermissions.READ_ALL,
        CommonUserPermissions.CREATE_OWN
    ),
    CommonUserGroups.SUPER_ADMIN to setOf(
        CommonUserPermissions.READ_OWN,
        CommonUserPermissions.READ_ALL,
        CommonUserPermissions.CREATE_OWN,
        CommonUserPermissions.DELETE_ALL,
        CommonUserPermissions.UPDATE_ALL
    ),
    CommonUserGroups.TEST to setOf(),
    CommonUserGroups.BAN_ORDER to setOf(),
)

private val groupPermissionsDenys = mapOf(
    CommonUserGroups.USER to setOf(),
    CommonUserGroups.SUPER_ADMIN to setOf(),
    CommonUserGroups.TEST to setOf(),
    CommonUserGroups.BAN_ORDER to setOf(
        CommonUserPermissions.CREATE_OWN,
    ),
)