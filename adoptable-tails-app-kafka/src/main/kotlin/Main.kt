package ru.otus.otuskotlin.adoptabletails.app.kafka

import ru.otus.otuskotlin.adoptabletails.app.kafka.configuration.KafkaConfiguration

fun main() {
    val config = KafkaConfiguration()
    val consumer = AdoptableTailsKafkaConsumer(config, listOf(ConsumerStrategyImpl()))
    consumer.run()
}