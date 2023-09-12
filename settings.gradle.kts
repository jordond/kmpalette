pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

plugins {
    id("com.gradle.enterprise") version "3.14.1"
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
    ":loader",
    ":extensions-bytearray",
    ":extensions-resources",
    ":extensions-libres",
    ":extensions-network",
)

include(":demo:composeApp")