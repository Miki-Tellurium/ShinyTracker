plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = project.property("projectGroup") as String
version = project.property("projectVersion") as String

repositories {
    mavenCentral()
    google()
}

dependencies {
    testImplementation(kotlin("test"))

    val kotlinxSerializationVersion = project.property("kotlinxSerializationVersion")
    val kotlinxDateTimeVersion = project.property("kotlinxDateTimeVersion")
    val slf4jVersion = project.property("slf4jVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDateTimeVersion")
    implementation("org.slf4j:slf4j-simple:$slf4jVersion")
}

kotlin {
    jvmToolchain(26)
}

javafx {
    version = project.property("javafxVersion") as String
    modules = listOf("javafx.controls", "javafx.fxml")
}

application {
    mainClass.set("com.mikitellurium.shinytracker.MainKt")
}

tasks.test {
    useJUnitPlatform()
}