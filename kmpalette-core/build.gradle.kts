plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.android.library)
    alias(libs.plugins.poko)
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
            baseName = "kmpalette-core"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":androidx-palette"))
                api(project(":kmpalette-bitmap-loader"))
                implementation(compose.ui)
                implementation(compose.material3)
                implementation(compose.runtime)
                implementation(libs.kotlinx.coroutines)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val browserMain by creating {
            dependsOn(commonMain)
            jsMain.get().dependsOn(this)
            wasmJsMain.get().dependsOn(this)
        }

        val nonJsMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.androidx.collection)
            }
        }

        val androidMain by getting {
            dependsOn(nonJsMain)
        }

        val jvmMain by getting {
            dependsOn(nonJsMain)
        }

        val nativeMain by getting {
            dependsOn(nonJsMain)
        }
    }
}

android {
    namespace = "com.kmpalette"

    compileSdk = libs.versions.sdk.compile.get().toInt()
    defaultConfig {
        minSdk = libs.versions.sdk.min.get().toInt()
    }

    kotlin {
        jvmToolchain(jdkVersion = 11)
    }
}