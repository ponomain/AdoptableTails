package ru.otus.otuskotlin.adoptabletails.common.permissions

import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAdId

data class CommonPrincipalModel(
    val id: PetAdId = PetAdId.NONE,
    val fname: String = "",
    val mname: String = "",
    val lname: String = "",
    val groups: Set<CommonUserGroups> = emptySet()
) {
    companion object {
        val NONE = CommonPrincipalModel()
    }
}