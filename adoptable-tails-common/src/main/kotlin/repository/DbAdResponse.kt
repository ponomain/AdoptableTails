package ru.otus.otuskotlin.adoptabletails.common.repository

import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsError
import ru.otus.otuskotlin.adoptabletails.common.models.advertisement.PetAd

data class DbAdResponse(
    override val data: PetAd?,
    override val isSuccess: Boolean,
    override val errors: List<AdoptableTailsError> = emptyList()
): DbResponse<PetAd> {
    companion object {
        val MOCK_SUCCESS_EMPTY = DbAdResponse(null, true)
        fun success(result: PetAd) = DbAdResponse(result, true)
        fun error(errors: List<AdoptableTailsError>, data: PetAd? = null) = DbAdResponse(data, false, errors)
        fun error(error: AdoptableTailsError, data: PetAd? = null) = DbAdResponse(data, false, listOf(error))

//        val errorEmptyId = error(commonErrorEmptyId)
//        val errorNotFound = error(commonErrorNotFound)
    }
}