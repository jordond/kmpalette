plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.poko)
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
            baseName = "androidx-palette"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.ui)
                implementation(libs.korim)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotlinx.coroutines.test)
            }
        }

        val skikoMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.korim)
            }
        }

        val nativeMain by getting {
            dependsOn(skikoMain)
        }

        val jsMain by getting {
            dependsOn(skikoMain)
        }

        val wasmJsMain by getting {
            dependsOn(skikoMain)
        }

        val androidInstrumentedTest by getting {
            dependencies {
                implementation(project(":extensions-base64"))
                implementation(kotlin("test"))
                implementation(compose.ui)
                implementation(libs.bundles.test.android)
                implementation(libs.androidx.core)
                implementation(libs.kotlinx.coroutines)
                implementation(libs.kotlinx.coroutines.test)
            }
        }
    }
}

android {
    namespace = "com.kmpalette.palette"

    compileSdk = libs.versions.sdk.compile.get().toInt()
    defaultConfig {
        minSdk = libs.versions.sdk.min.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    sourceSets["main"].apply {
        res.srcDirs("src/androidInstrumentedTest/res")
    }

    kotlin {
        jvmToolchain(jdkVersion = 11)
    }
}