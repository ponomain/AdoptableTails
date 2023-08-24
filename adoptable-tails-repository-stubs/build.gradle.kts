plugins {
    kotlin("jvm")
    kotlin("kapt")
}

dependencies {
    implementation(project(":adoptable-tails-common"))
    implementation(project(":adoptable-tails-stubs"))

    testImplementation(project(":adoptable-tails-repository-tests"))

    testImplementation(kotlin("test-junit"))
    testImplementation(kotlin("test"))
}