plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    val datetimeVersion: String by project

    implementation(kotlin("stdlib"))
    implementation(project(":adoptable-tails-api"))
    implementation(project(":adoptable-tails-common"))
    api("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")
    testImplementation(kotlin("test-junit"))
}