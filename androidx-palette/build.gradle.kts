@file:Suppress("UNUSED_VARIABLE", "OPT_IN_USAGE")

import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.dokka)
    alias(libs.plugins.publish)
}

kotlin {
//    explicitApi = ExplicitApiMode.Strict

    targetHierarchy.default()

    androidTarget {
        publishAllLibraryVariants()
    }

    jvm()

//    js(IR) {
//        browser()
//    }

    macosX64()
    macosArm64()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "androidx-palette"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.ui)
                implementation(libs.kotlinx.immutableCollections)
                implementation("androidx.annotation:annotation:1.7.0")
                implementation("androidx.collection:collection:1.3.0-rc01")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val nativeMain by getting {
            dependencies {
                implementation("com.soywiz.korlibs.korim:korim:4.0.10")
            }
        }
    }
}

android {
    namespace = "dev.jordond.kmpalette.palette"

    compileSdk = libs.versions.sdk.compile.get().toInt()
    defaultConfig {
        minSdk = libs.versions.sdk.min.get().toInt()
    }

    kotlin {
        jvmToolchain(jdkVersion = 11)
    }
}