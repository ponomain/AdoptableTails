package ru.otus.otuskotlin.adoptabletails.app.kafka

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.Test
import ru.otus.otuskotlin.adoptabletails.api.apiV1RequestSerialize
import ru.otus.otuskotlin.adoptabletails.api.apiV1ResponseDeserialize
import ru.otus.otuskotlin.adoptabletails.app.kafka.configuration.KafkaConfiguration
import ru.otus.otuskotlin.api.models.PetAdCreateObject
import ru.otus.otuskotlin.api.models.PetAdCreateRequest
import ru.otus.otuskotlin.api.models.PetAdCreateResponse
import ru.otus.otuskotlin.api.models.PetAdDebug
import ru.otus.otuskotlin.api.models.PetAdRequestDebugMode
import ru.otus.otuskotlin.api.models.PetAdRequestDebugStubs
import ru.otus.otuskotlin.api.models.PetTemperament
import ru.otus.otuskotlin.api.models.PetType
import java.math.BigDecimal
import java.util.*
import kotlin.test.assertEquals

class KafkaControllerTest {
    @Test
    fun runKafka() {
        val consumer = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
        val producer = MockProducer(true, StringSerializer(), StringSerializer())
        val config = KafkaConfiguration()
        val inputTopic = config.kafkaTopicInV1
        val outputTopic = config.kafkaTopicOutV1

        val app = AdoptableTailsKafkaConsumer(
            config,
            listOf(ConsumerStrategyImpl()),
            consumer = consumer,
            producer = producer
        )
        consumer.schedulePollTask {
            consumer.rebalance(Collections.singletonList(TopicPartition(inputTopic, 0)))
            consumer.addRecord(
                ConsumerRecord(
                    inputTopic,
                    PARTITION,
                    0L,
                    "test-1",
                    apiV1RequestSerialize(
                        PetAdCreateRequest(
                            requestType = "create",
                            requestId = "11111111-1111-1111-1111-111111111111",
                            petAd = PetAdCreateObject(
                                name = "Some Name",
                                description = "some testing order to check them all",
                                breed = "some breed",
                                age = BigDecimal.TEN,
                                temperament = PetTemperament.SANGUINE.name,
                                propertySize = "above average"
                            ),
                            debug = PetAdDebug(
                                mode = PetAdRequestDebugMode.STUB,
                                stub = PetAdRequestDebugStubs.SUCCESS
                            )
                        )
                    )
                )
            )
            app.stop()
        }

        val startOffsets: MutableMap<TopicPartition, Long> = mutableMapOf()
        val tp = TopicPartition(inputTopic, PARTITION)
        startOffsets[tp] = 0L
        consumer.updateBeginningOffsets(startOffsets)

        app.run()

        val message = producer.history().first()
        val result = apiV1ResponseDeserialize<PetAdCreateResponse>(message.value())
        assertEquals(outputTopic, message.topic())
        assertEquals("11111111-1111-1111-1111-111111111111", result.requestId)
        assertEquals("Some Name", result.petAd?.name)
    }

    companion object {
        const val PARTITION = 0
    }
}