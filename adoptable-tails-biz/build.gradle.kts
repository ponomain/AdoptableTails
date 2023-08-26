plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    val coroutinesVersion: String by project
    val kotestVersion: String by project

    testImplementation(kotlin("test-junit"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")

    implementation(project(":adoptable-tails-lib-cor"))
    implementation(project(":adoptable-tails-common"))
    implementation(project(":adoptable-tails-stubs"))
    implementation(project(":adoptable-tails-repository-cassandra"))
    implementation(project(":adoptable-tails-repository-stubs"))
    implementation(project(":adoptable-tails-repository-tests"))
    implementation(project(":adoptable-tails-authorization"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")

}

tasks.test {
    useJUnitPlatform()
}