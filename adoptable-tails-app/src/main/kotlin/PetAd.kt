package ru.otus.otuskotlin.adoptabletails
class PetAd {
    var breed: String = ""
    var name: String = ""
    var status: String = "CREATED"

    fun setBreedAndName(breed: String, name: String) {
        this.breed = breed
        this.name = name
    }
}