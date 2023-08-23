package ru.otus.otuskotlin.adoptabletails.lib.logback

import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory
import ru.otus.otuskotlin.adoptabletails.lib.log.common.LogWrapper
import kotlin.reflect.KClass

fun mpLoggerLogback(logger: Logger): LogWrapper = LogWrapperLogback(
    logger = logger,
    loggerId = logger.name,
)

fun mpLoggerLogback(clazz: KClass<*>): LogWrapper = mpLoggerLogback(LoggerFactory.getLogger(clazz.java) as Logger)

fun mpLoggerLogback(loggerId: String): LogWrapper = mpLoggerLogback(LoggerFactory.getLogger(loggerId) as Logger)