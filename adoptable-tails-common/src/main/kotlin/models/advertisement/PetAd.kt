package ru.otus.otuskotlin.adoptabletails.common.models.advertisement

import ru.otus.otuskotlin.adoptabletails.common.permissions.CommonAdPermissionClient
import ru.otus.otuskotlin.adoptabletails.common.permissions.CommonPrincipalRelations
import java.math.BigDecimal
import java.time.Instant

data class PetAd(
    var id: PetAdId = PetAdId.NONE,
    var name: String = "",
    var breed: String = "",
    var age: BigDecimal = BigDecimal.ZERO,
    var temperament: PetTemperament = PetTemperament.NONE,
    var size: String = "",
    var description: String = "",
    var createdAt: Instant = Instant.MIN,
    var updatedAt: Instant = Instant.MIN,
    var petAdStatus: PetAdStatus = PetAdStatus.CREATED,
    var principalRelations: Set<CommonPrincipalRelations> = emptySet(),
    val permissionsClient: MutableSet<CommonAdPermissionClient> = mutableSetOf()
) {
    fun deepCopy(): PetAd = copy(
        principalRelations = principalRelations.toSet(),
        permissionsClient = permissionsClient.toMutableSet(),
    )
}