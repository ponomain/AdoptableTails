plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    val coroutinesVersion: String by project
    val datetimeVersion: String by project
    val logbackVersion: String by project
    val logbackEncoderVersion: String by project

    implementation("net.logstash.logback:logstash-logback-encoder:$logbackEncoderVersion")
    api("ch.qos.logback:logback-classic:$logbackVersion")
    implementation(kotlin("stdlib-common"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    api("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")

}