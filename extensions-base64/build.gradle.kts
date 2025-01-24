plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.android.library)
    alias(libs.plugins.dokka)
    alias(libs.plugins.publish)
    alias(libs.plugins.kover)
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

    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "extensions-base64"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":kmpalette-core"))
                api(project(":androidx-palette"))
                api(project(":kmpalette-bitmap-loader"))
                api(project(":extensions-bytearray"))
                implementation(compose.ui)
                implementation(libs.kotlinx.coroutines)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(project(":test-utils"))
                implementation(kotlin("test"))
                implementation(compose.ui)
                implementation(libs.kotlinx.coroutines.test)
            }
        }

        val androidInstrumentedTest by getting {
            dependencies {
                implementation(project(":test-utils"))
                implementation(kotlin("test"))
                implementation(compose.ui)
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.bundles.test.android)
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
    }
}

android {
    namespace = "com.kmpalette.extensions.base64"

    compileSdk = libs.versions.sdk.compile.get().toInt()
    defaultConfig {
        minSdk = libs.versions.sdk.min.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    kotlin {
        jvmToolchain(jdkVersion = 11)
    }
}