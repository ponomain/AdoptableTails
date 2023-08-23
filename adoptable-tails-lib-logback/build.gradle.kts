plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    val logbackVersion: String by project
    val logbackEncoderVersion: String by project
    val coroutinesVersion: String by project
    val janinoVersion: String by project
    val datetimeVersion: String by project

    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":adoptable-tails-lib-log-common"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")
    implementation("net.logstash.logback:logstash-logback-encoder:$logbackEncoderVersion")
    implementation("org.codehaus.janino:janino:$janinoVersion")
    api("ch.qos.logback:logback-classic:$logbackVersion")
    testImplementation(kotlin("test-junit"))
}