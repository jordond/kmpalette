pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven { url = uri("https://jitpack.io") }
    }
}

plugins {
    id("com.gradle.enterprise") version "3.16.2"
}

gradleEnterprise {
    if (System.getenv("CI") != null) {
        buildScan {
            publishAlways()
            termsOfServiceUrl = "https://gradle.com/terms-of-service"
            termsOfServiceAgree = "yes"
        }
    }
}

rootProject.name = "kmpalette"

include(
    ":kmpalette-core",
    ":androidx-palette",
    ":kmpalette-bitmap-loader",
    ":extensions-base64",
    ":extensions-bytearray",
    ":extensions-resources",
    ":extensions-libres",
    ":extensions-network",
    ":extensions-file",
    ":test-utils",
)

include(":demo:composeApp")