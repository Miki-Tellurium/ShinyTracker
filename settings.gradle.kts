pluginManagement {
    val kotlinVersion = providers.gradleProperty("kotlinVersion")

    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }

    plugins {
        id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
    }
}

rootProject.name = "ShinyTracker"