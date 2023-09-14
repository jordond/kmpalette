@file:Suppress("OPT_IN_USAGE")

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.android.library)
    alias(libs.plugins.dokka)
    alias(libs.plugins.publish)
}

kotlin {
    explicitApi()

    targetHierarchy.default()

    androidTarget {
        publishAllLibraryVariants()
    }

    jvm()

    macosX64()
    macosArm64()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "extensions-bytearray"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":kmpalette-core"))
                api(project(":kmpalette-bitmap-loader"))
                implementation(compose.ui)
                implementation(libs.kotlinx.coroutines)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val skikoMain by creating {
            dependsOn(commonMain)
        }

        val nativeMain by getting {
            dependsOn(skikoMain)
        }

        val jvmMain by getting {
            dependsOn(skikoMain)
        }
    }
}

android {
    namespace = "dev.jordond.kmpalette.extensions.bytearray"

    compileSdk = libs.versions.sdk.compile.get().toInt()
    defaultConfig {
        minSdk = libs.versions.sdk.min.get().toInt()
    }

    kotlin {
        jvmToolchain(jdkVersion = 11)
    }
}