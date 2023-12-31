package ru.otus.otuskotlin.adoptabletails.common.models.helpers

import ru.otus.otuskotlin.adoptabletails.common.AdoptableTailsContext
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsError
import ru.otus.otuskotlin.adoptabletails.common.models.AdoptableTailsState


fun Throwable.asAdoptableTailsError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = AdoptableTailsError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this
)

fun AdoptableTailsContext.addError(vararg error: AdoptableTailsError) = errors.addAll(error)

fun AdoptableTailsContext.fail(error: AdoptableTailsError) {
    addError(error)
    state = AdoptableTailsState.FAILING
}

fun errorValidation(
    field: String,
    violationCode: String,
    description: String,
    level: AdoptableTailsError.Level = AdoptableTailsError.Level.ERROR
) = AdoptableTailsError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)

fun errorAdministration(
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    field: String = "",
    violationCode: String,
    description: String,
    exception: Exception? = null,
) = AdoptableTailsError(
    field = field,
    code = "administration-$violationCode",
    group = "administration",
    message = "Microservice management error: $description",
    exception = exception,
)