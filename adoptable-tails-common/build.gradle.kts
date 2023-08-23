plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    val datetimeVersion: String by project

    testImplementation(kotlin("test-junit"))
    api("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")
    api(project(":adoptable-tails-lib-log-common"))
}