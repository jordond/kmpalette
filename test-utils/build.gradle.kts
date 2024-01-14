plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.android.library)
    alias(libs.plugins.libres)
}

kotlin {
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
            baseName = "test-utils"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":kmpalette-core"))
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
                implementation(compose.ui)
                implementation(libs.kotlinx.coroutines)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(compose.ui)
                implementation(libs.kotlinx.coroutines.test)
            }
        }

        val skikoMain by creating {
            dependsOn(commonMain)
        }

        val nativeMain by getting {
            dependsOn(skikoMain)
        }

        val jsAndWasmJsMain by creating {
            dependsOn(skikoMain)
        }

        val androidInstrumentedTest by getting {
            dependencies {
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
    namespace = "com.kmpalette.test"

    compileSdk = libs.versions.sdk.compile.get().toInt()
    defaultConfig {
        minSdk = libs.versions.sdk.min.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    sourceSets["main"].apply {
        res.srcDirs("src/androidMain/resources")
        resources.srcDirs("src/commonMain/resources")
    }

    kotlin {
        jvmToolchain(jdkVersion = 11)
    }
}