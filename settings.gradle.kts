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
    id("com.gradle.develocity") version "3.19.2"
}

develocity {
    buildScan {
        termsOfUseUrl.set("https://gradle.com/help/legal-terms-of-use")
        termsOfUseAgree.set("yes")

        publishing.onlyIf { context ->
            context.buildResult.failures.isNotEmpty() && !System.getenv("CI").isNullOrEmpty()
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
    ":extensions-network",
    ":extensions-file",
    ":test-utils",
)

include(":demo:composeApp")