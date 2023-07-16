rootProject.name = "AdoptableTails"

pluginManagement {
    val kotlinVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
    }
}
include("adoptable-tails-init")

