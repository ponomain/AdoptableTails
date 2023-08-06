rootProject.name = "AdoptableTails"

pluginManagement {
    val kotlinVersion: String by settings
    val openapiVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion apply false
        id("org.openapi.generator") version openapiVersion apply false
    }
}
include("adoptable-tails-init")
include("adoptable-tails-app")
include("adoptable-tails-api")
include("adoptable-tails-common")
include("adoptable-tails-mappers")
