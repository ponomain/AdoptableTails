package ru.otus.otuskotlin.adoptabletails.common.models.advertisement

@JvmInline
value class PetAdId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = PetAdId("")
    }
}