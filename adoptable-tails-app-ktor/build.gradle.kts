import org.jetbrains.kotlin.util.suffixIfNot
import com.bmuschko.gradle.docker.tasks.image.Dockerfile
import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage

val ktorVersion: String by project
val logbackVersion: String by project
val kotestVersion: String by project
val ktorKotestExtensionVersion: String by project
val datetimeVersion: String by project

fun ktor(
    module: String,
    prefix: String = "server-",
    version: String? = this@Build_gradle.ktorVersion
): Any = "io.ktor:ktor-${prefix.suffixIfNot("-")}$module:$version"

fun ktorServer(module: String, version: String? = this@Build_gradle.ktorVersion): Any =
    "io.ktor:ktor-server-$module:$version"

fun ktorClient(module: String, version: String? = this@Build_gradle.ktorVersion): Any =
    "io.ktor:ktor-client-$module:$version"

plugins {
    kotlin("jvm")
    id("io.ktor.plugin")
    id("com.bmuschko.docker-java-application")
    id("com.bmuschko.docker-remote-api")
    kotlin("plugin.serialization")
}

application {
    mainClass.set("ru.otus.otuskotlin.adoptabletails.app.ktor.ApplicationKt")
}

repositories {
    mavenCentral()
}

ktor {
    docker {
        localImageName.set(project.name)
        imageTag.set(project.version.toString())
        jreVersion.set(io.ktor.plugin.features.JreVersion.JRE_11)
    }
}

jib {
    container.mainClass = "ru.otus.otuskotlin.adoptabletails.app.ktor.ApplicationKt"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(ktorServer("core"))
    implementation(ktorServer("netty"))
    implementation(ktorServer("config-yaml"))

    implementation(ktorServer("content-negotiation"))
    implementation(ktor("jackson", "serialization"))
    implementation(ktor("kotlinx-json", "serialization"))

    implementation(ktor("locations"))
    implementation(ktor("caching-headers"))
    implementation(ktorServer("call-logging"))
    implementation(ktor("auto-head-response"))
    implementation(ktor("cors"))
    implementation(ktor("default-headers"))
    implementation(ktor("websockets"))

    testImplementation(kotlin("test-junit"))

    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    implementation(ktorServer("auth-jwt"))
    implementation(ktorServer("auth"))

    implementation(project(mapOf("path" to ":adoptable-tails-common")))
    implementation(project(mapOf("path" to ":adoptable-tails-api")))
    implementation(project(mapOf("path" to ":adoptable-tails-mappers")))
    implementation(project(mapOf("path" to ":adoptable-tails-stubs")))
    implementation(project(mapOf("path" to ":adoptable-tails-biz")))
    implementation(project(mapOf("path" to ":adoptable-tails-lib-logback")))
    implementation(project(mapOf("path" to ":adoptable-tails-mappers-log")))
    implementation(project(mapOf("path" to ":adoptable-tails-api-log")))
    implementation(project(mapOf("path" to ":adoptable-tails-repository-cassandra")))
    implementation(project(mapOf("path" to ":adoptable-tails-repository-stubs")))
    implementation(project(mapOf("path" to ":adoptable-tails-repository-tests")))

    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest.extensions:kotest-assertions-ktor:${ktorKotestExtensionVersion}")
    testImplementation(ktorServer("test-host"))
    testImplementation(ktorClient("content-negotiation"))
    testImplementation(ktor("websockets", prefix = "client-"))
}


tasks.test {
    useJUnitPlatform()
}
