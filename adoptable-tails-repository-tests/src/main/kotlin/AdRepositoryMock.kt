package ru.otus.otuskotlin.adoptabletails.repository.tests

import ru.otus.otuskotlin.adoptabletails.common.repository.AdRepository
import ru.otus.otuskotlin.adoptabletails.common.repository.DbAdFilterRequest
import ru.otus.otuskotlin.adoptabletails.common.repository.DbAdIdRequest
import ru.otus.otuskotlin.adoptabletails.common.repository.DbAdRequest
import ru.otus.otuskotlin.adoptabletails.common.repository.DbAdResponse
import ru.otus.otuskotlin.adoptabletails.common.repository.DbAdsResponse

class AdRepositoryMock(
    private val invokeCreateAd: (DbAdRequest) -> DbAdResponse = { DbAdResponse.MOCK_SUCCESS_EMPTY },
    private val invokeReadAd: (DbAdIdRequest) -> DbAdResponse = { DbAdResponse.MOCK_SUCCESS_EMPTY },
    private val invokeDeleteAd: (DbAdIdRequest) -> DbAdResponse = { DbAdResponse.MOCK_SUCCESS_EMPTY },
    private val invokeSearchAd: (DbAdFilterRequest) -> DbAdsResponse = { DbAdsResponse.MOCK_SUCCESS_EMPTY },
) : AdRepository {
    override suspend fun createAd(rq: DbAdRequest): DbAdResponse {
        return invokeCreateAd(rq)
    }

    override suspend fun readAd(rq: DbAdIdRequest): DbAdResponse {
        return invokeReadAd(rq)
    }

    override suspend fun deleteAd(rq: DbAdIdRequest): DbAdResponse {
        return invokeDeleteAd(rq)
    }

    override suspend fun searchAd(rq: DbAdFilterRequest): DbAdsResponse {
        return invokeSearchAd(rq)
    }
}