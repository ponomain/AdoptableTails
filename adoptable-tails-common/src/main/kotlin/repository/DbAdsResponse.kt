package ru.otus.otuskotlin.adoptabletails.common.repository

import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsError
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAd

data class DbAdsResponse(
    override val data: List<PetAd>?,
    override val isSuccess: Boolean,
    override val errors: List<AdoptableTailsError> = emptyList()
): DbResponse<List<PetAd>> {
    companion object {
        val MOCK_SUCCESS_EMPTY = DbAdsResponse(emptyList(), true)
        fun success(result: List<PetAd>) = DbAdsResponse(result, true)
        fun error(errors: List<AdoptableTailsError>) = DbAdsResponse(null, false, errors)
        fun error(error: AdoptableTailsError) = DbAdsResponse(null, false, listOf(error))

//        val errorEmptyId = error(commonErrorEmptyId)
//        val errorNotFound = error(commonErrorNotFound)
    }
}