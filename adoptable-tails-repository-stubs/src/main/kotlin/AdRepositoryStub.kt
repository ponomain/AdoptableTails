package ru.otus.otuskotlin.adoptabletails.repository.stubs

import ru.otus.otuskotlin.adoptabletails.common.repository.AdRepository
import ru.otus.otuskotlin.adoptabletails.common.repository.DbAdFilterRequest
import ru.otus.otuskotlin.adoptabletails.common.repository.DbAdIdRequest
import ru.otus.otuskotlin.adoptabletails.common.repository.DbAdRequest
import ru.otus.otuskotlin.adoptabletails.common.repository.DbAdResponse
import ru.otus.otuskotlin.adoptabletails.common.repository.DbAdsResponse
import ru.otus.otuskotlin.adoptabletails.stubs.PetAdStub

class AdRepositoryStub : AdRepository {
    override suspend fun createAd(rq: DbAdRequest): DbAdResponse {
        return DbAdResponse(
            data = PetAdStub.getPetAd(),
            isSuccess = true
        )
    }

    override suspend fun readAd(rq: DbAdIdRequest): DbAdResponse {
        return DbAdResponse(
            data = PetAdStub.getPetAd(),
            isSuccess = true
        )
    }

    override suspend fun deleteAd(rq: DbAdIdRequest): DbAdResponse {
        return DbAdResponse(
            data = PetAdStub.getPetAd(),
            isSuccess = true
        )
    }

    override suspend fun searchAd(rq: DbAdFilterRequest): DbAdsResponse {
        return DbAdsResponse(
            data = PetAdStub.getPetAds(),
            isSuccess = true
        )
    }
}