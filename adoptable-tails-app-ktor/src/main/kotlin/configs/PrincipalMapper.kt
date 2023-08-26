package ru.otus.otuskotlin.adoptabletails.app.ktor.configs

import io.ktor.server.auth.jwt.*
import ru.otus.otuskotlin.adoptabletails.app.ktor.configs.KtorAuthConfig.Companion.F_NAME_CLAIM
import ru.otus.otuskotlin.adoptabletails.app.ktor.configs.KtorAuthConfig.Companion.GROUPS_CLAIM
import ru.otus.otuskotlin.adoptabletails.app.ktor.configs.KtorAuthConfig.Companion.ID_CLAIM
import ru.otus.otuskotlin.adoptabletails.app.ktor.configs.KtorAuthConfig.Companion.L_NAME_CLAIM
import ru.otus.otuskotlin.adoptabletails.app.ktor.configs.KtorAuthConfig.Companion.M_NAME_CLAIM
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAdId
import ru.otus.otuskotlin.adoptabletails.common.permissions.CommonPrincipalModel
import ru.otus.otuskotlin.adoptabletails.common.permissions.CommonUserGroups

fun JWTPrincipal?.toModel() = this?.run {
    CommonPrincipalModel(
        id = payload.getClaim(ID_CLAIM).asString()?.let { PetAdId(it) } ?: PetAdId.NONE,
        fname = payload.getClaim(F_NAME_CLAIM).asString() ?: "",
        mname = payload.getClaim(M_NAME_CLAIM).asString() ?: "",
        lname = payload.getClaim(L_NAME_CLAIM).asString() ?: "",
        groups = payload
            .getClaim(GROUPS_CLAIM)
            ?.asList(String::class.java)
            ?.mapNotNull {
                when(it) {
                    "USER" -> CommonUserGroups.USER
                    else -> null
                }
            }?.toSet() ?: emptySet()
    )
} ?: CommonPrincipalModel.NONE