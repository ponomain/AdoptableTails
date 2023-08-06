package ru.otus.otuskotlin.adoptabletails.common.models

@JvmInline
value class PetAdRequestId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = PetAdRequestId("")
    }
}