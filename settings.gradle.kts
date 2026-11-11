pluginManagement {
    val kotlinVersion = providers.gradleProperty("kotlinVersion")
    val composeVersion = providers.gradleProperty("composeVersion")

    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }

    plugins {
        id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
        kotlin("plugin.compose") version kotlinVersion
        id("org.jetbrains.compose") version composeVersion
    }
}

rootProject.name = "ShinyTracker"