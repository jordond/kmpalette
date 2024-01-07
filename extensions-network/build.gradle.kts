plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.android.library)
    alias(libs.plugins.dokka)
    alias(libs.plugins.publish)
}

kotlin {
    explicitApi()

    applyDefaultHierarchyTemplate()

    androidTarget {
        publishAllLibraryVariants()
    }

    jvm()

    js(IR) {
        browser()
    }

    @OptIn(org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    macosX64()
    macosArm64()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "extensions-network"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":kmpalette-core"))
                api(project(":kmpalette-bitmap-loader"))
                implementation(project(":extensions-bytearray"))
                implementation(compose.ui)
                implementation(libs.kotlinx.coroutines)
                implementation(libs.ktor.client)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.startup)
                implementation(libs.androidx.core)
            }
        }
    }
}

android {
    namespace = "com.kmpalette.extensions.network"

    compileSdk = libs.versions.sdk.compile.get().toInt()
    defaultConfig {
        minSdk = libs.versions.sdk.min.get().toInt()
    }

    kotlin {
        jvmToolchain(jdkVersion = 11)
    }
}