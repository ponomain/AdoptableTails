package ru.otus.otuskotlin.adoptabletails.app.kafka

import kotlinx.atomicfu.atomic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.errors.WakeupException
import ru.otus.otuskotlin.adoptabletails.app.kafka.configuration.KafkaConfiguration
import ru.otus.otuskotlin.adoptabletails.app.kafka.utils.createKafkaConsumer
import ru.otus.otuskotlin.adoptabletails.app.kafka.utils.createKafkaProducer
import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsContext
import ru.otus.otuskotlin.adoptabletails.stubs.PetAdStub
import java.time.Duration
import java.util.*

private val log = KotlinLogging.logger {}

data class InputOutputTopics(val input: String, val output: String)

class PetAdProcessor {
    fun exec(ctx: AdoptableTailsContext) {
        when (ctx) {
            is AdoptableTailsContext -> ctx.petAdResponse = PetAdStub.getPetAd()
        }
    }
}

interface ConsumerStrategy {
    fun topics(config: KafkaConfiguration): InputOutputTopics
    fun serialize(source: AdoptableTailsContext): String
    fun deserialize(value: String, target: AdoptableTailsContext)
}

class AdoptableTailsKafkaConsumer(
    private val config: KafkaConfiguration,
    consumerStrategies: List<ConsumerStrategyImpl>,
    private val processor: PetAdProcessor = PetAdProcessor(),
    private val consumer: Consumer<String, String> = config.createKafkaConsumer(),
    private val producer: Producer<String, String> = config.createKafkaProducer()
) {
    private val process = atomic(true)
    private val topicsAndStrategyByInputTopic = consumerStrategies.associate {
        val topics = it.topics(config)
        Pair(
            topics.input,
            TopicsAndStrategy(topics.input, topics.output, it)
        )
    }

    fun run() = runBlocking {
        try {
            consumer.subscribe(topicsAndStrategyByInputTopic.keys)
            while (process.value) {
                val ctx = AdoptableTailsContext(
                    timeStart = Clock.System.now(),
                )
                val records: ConsumerRecords<String, String> = withContext(Dispatchers.IO) {
                    consumer.poll(Duration.ofSeconds(1))
                }
                if (!records.isEmpty)
                    log.info { "Receive ${records.count()} messages" }

                records.forEach { record: ConsumerRecord<String, String> ->
                    try {
                        log.info { "process ${record.key()} from ${record.topic()}:\n${record.value()}" }
                        val (_, outputTopic, strategy) = topicsAndStrategyByInputTopic[record.topic()]
                            ?: throw RuntimeException("Receive message from unknown topic ${record.topic()}")

                        strategy.deserialize(record.value(), ctx)
                        processor.exec(ctx)

                        sendResponse(ctx, strategy, outputTopic)
                    } catch (ex: Exception) {
                        log.error(ex) { "error" }
                    }
                }
            }
        } catch (ex: WakeupException) {
            // ignore for shutdown
        } catch (ex: RuntimeException) {
            // exception handling
            withContext(NonCancellable) {
                throw ex
            }
        } finally {
            withContext(NonCancellable) {
                consumer.close()
            }
        }
    }

    private fun sendResponse(context: AdoptableTailsContext, strategy: ConsumerStrategyImpl, outputTopic: String) {
        val json = strategy.serialize(context)
        val resRecord = ProducerRecord(
            outputTopic,
            UUID.randomUUID().toString(),
            json
        )
        log.info { "sending ${resRecord.key()} to $outputTopic:\n$json" }
        producer.send(resRecord)
    }

    fun stop() {
        process.value = false
    }

    private data class TopicsAndStrategy(
        val inputTopic: String,
        val outputTopic: String,
        val strategy: ConsumerStrategyImpl
    )
}