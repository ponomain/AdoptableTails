package ru.otus.otuskotlin.adoptabletails.app.kafka

import ru.otus.otuskotlin.adoptabletails.api.apiV1RequestDeserialize
import ru.otus.otuskotlin.adoptabletails.api.apiV1ResponseSerialize
import ru.otus.otuskotlin.adoptabletails.app.kafka.configuration.KafkaConfiguration
import ru.otus.otuskotlin.adoptabletails.common.PetAdContext
import ru.otus.otuskotlin.adoptabletails.mappers.mapper.fromTransportPetAd
import ru.otus.otuskotlin.adoptabletails.mappers.mapper.toTransportPetAd
import ru.otus.otuskotlin.api.models.IRequest
import ru.otus.otuskotlin.api.models.IResponse

class ConsumerStrategyImpl : ConsumerStrategy {
    override fun topics(config: KafkaConfiguration): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicInV1, config.kafkaTopicOutV1)
    }

    override fun serialize(source: PetAdContext): String {
        val response: IResponse = source.toTransportPetAd()
        return apiV1ResponseSerialize(response)
    }

    override fun deserialize(value: String, target: PetAdContext) {
        val request: IRequest = apiV1RequestDeserialize(value)
        target.fromTransportPetAd(request)
    }
}