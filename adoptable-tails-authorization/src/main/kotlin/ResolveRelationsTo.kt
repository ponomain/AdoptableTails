package ru.otus.otuskotlin.adoptabletails.authorization

import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAd
import ru.otus.otuskotlin.adoptabletails.common.permissions.CommonPrincipalModel
import ru.otus.otuskotlin.adoptabletails.common.permissions.CommonPrincipalRelations

fun PetAd.resolveRelationsTo(principal: CommonPrincipalModel): Set<CommonPrincipalRelations> = setOfNotNull(
    CommonPrincipalRelations.NONE,
    CommonPrincipalRelations.OWN.takeIf { principal.id == id },
)