package ru.otus.otuskotlin.adoptabletails.app.ktor.app

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.otus.otuskotlin.adoptabletails.app.ktor.AdoptableTailsAppSettings
import ru.otus.otuskotlin.adoptabletails.app.ktor.configs.toModel
import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsContext
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsCommand
import ru.otus.otuskotlin.adoptabletails.lib.log.common.LogWrapper
import ru.otus.otuskotlin.adoptabletails.mappers.log.toLog
import ru.otus.otuskotlin.adoptabletails.mappers.mapper.fromTransportPetAd
import ru.otus.otuskotlin.adoptabletails.mappers.mapper.toTransportPetAd
import ru.otus.otuskotlin.api.models.IRequest


suspend inline fun <reified Q : IRequest> ApplicationCall.petAdProcess(
    appSettings: AdoptableTailsAppSettings,
    logger: LogWrapper,
    logId: String,
    command: AdoptableTailsCommand,
) {
    val ctx = AdoptableTailsContext(
        timeStart = kotlinx.datetime.Clock.System.now(),
    )
    try {
        logger.doWithLogging(id = logId) {
            val request = receive<Q>()
            ctx.fromTransportPetAd(request)
            ctx.principal = principal<JWTPrincipal>().toModel()
            logger.info(
                msg = "$command request is got",
                data = ctx.toLog("${logId}-got")
            )
            appSettings.adoptableTailsProcessor.exec(ctx)
            logger.info(
                msg = "$command request is handled",
                data = ctx.toLog("${logId}-handled")
            )
            respond(ctx.toTransportPetAd())
        }
    } catch (e: Throwable) {
        logger.doWithLogging(id = "${logId}-failure") {
            command.also { ctx.command = it }
            logger.error(
                msg = "$command handling failed",
                e = e,
                data = ctx.toLog(logger.loggerId)
            )
        }
    }
}
