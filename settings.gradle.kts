rootProject.name = "AdoptableTails"

pluginManagement {
    val kotlinVersion: String by settings
    val openapiVersion: String by settings
    val ktorPluginVersion: String by settings
    val bmuschkoVersion: String by settings


    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion apply false
        id("org.openapi.generator") version openapiVersion apply false
        id("io.ktor.plugin") version ktorPluginVersion apply false
        id("com.bmuschko.docker-java-application") version bmuschkoVersion apply false
        id("com.bmuschko.docker-remote-api") version bmuschkoVersion apply false
    }
}
include("adoptable-tails-init")
include("adoptable-tails-app")
include("adoptable-tails-api")
include("adoptable-tails-common")
include("adoptable-tails-mappers")
include("adoptable-tails-app-ktor")
include("adoptable-tails-stubs")
include("adoptable-tails-app-kafka")
include("adoptable-tails-biz")
include("adoptable-tails-lib-cor")
include("adoptable-tails-lib-log-common")
include("adoptable-tails-lib-logback")
include("adoptable-tails-mappers-log")
include("adoptable-tails-api-log")
include("adoptable-tails-repository-cassandra")
include("adoptable-tails-repository-stubs")
include("adoptable-tails-repository-tests")
include("adoptable-tails-authorization")

