package ru.otus.otuskotlin.adoptabletails.common.repository

interface AdRepository {
    suspend fun createAd(rq: DbAdRequest): DbAdResponse
    suspend fun readAd(rq: DbAdIdRequest): DbAdResponse
    suspend fun updateAd(rq: DbAdUpdateRequest): DbAdResponse
    suspend fun deleteAd(rq: DbAdIdRequest): DbAdResponse
    suspend fun searchAd(rq: DbAdFilterRequest): DbAdsResponse
    companion object {
        val NONE = object : AdRepository {
            override suspend fun createAd(rq: DbAdRequest): DbAdResponse {
                TODO("Not yet implemented")
            }
            override suspend fun readAd(rq: DbAdIdRequest): DbAdResponse {
                TODO("Not yet implemented")
            }
            override suspend fun updateAd(rq: DbAdUpdateRequest): DbAdResponse {
                TODO("Not yet implemented")
            }
            override suspend fun deleteAd(rq: DbAdIdRequest): DbAdResponse {
                TODO("Not yet implemented")
            }
            override suspend fun searchAd(rq: DbAdFilterRequest): DbAdsResponse {
                TODO("Not yet implemented")
            }
        }
    }
}