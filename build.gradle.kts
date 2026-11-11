plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    kotlin("plugin.compose")
    id("org.jetbrains.compose")
}

group = project.property("projectGroup") as String
version = project.property("projectVersion") as String

repositories {
    mavenCentral()
    google()
}

dependencies {
    val composeVersion = project.property("composeVersion")
    val kotlinxSerializationVersion = project.property("kotlinxSerializationVersion")
    val slf4jVersion = project.property("slf4jVersion")

    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.compose.foundation:foundation:${composeVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")
    implementation("org.slf4j:slf4j-simple:$slf4jVersion")

    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(26)
}

compose.desktop {
    application {
        mainClass = "com.mikitellurium.shinytracker.MainKt"
    }
}

tasks.test {
    useJUnitPlatform()
}