package ru.otus.otuskotlin.adoptabletails.common.models

@JvmInline
value class AdoptableTailsRequestId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = AdoptableTailsRequestId("")
    }
}