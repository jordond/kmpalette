import org.jetbrains.kotlin.gradle.dsl.JvmTarget

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
    jvmToolchain(jdkVersion = 11)
    applyDefaultHierarchyTemplate()

    androidLibrary {
        namespace = "${libs.versions.group.get()}.extensions.base64"
        compileSdk =
            libs.versions.sdk.compile
                .get()
                .toInt()
        minSdk =
            libs.versions.sdk.min
                .get()
                .toInt()

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    jvm()

    js(IR) {
        browser()
        binaries.library()
    }

    @Suppress("OPT_IN_USAGE")
    wasmJs {
        browser()
        binaries.library()
    }

    macosX64()
    macosArm64()

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { target ->
        target.binaries.framework {
            baseName = "kmpalette-extensions-base64"
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.kmpaletteCore)
            api(projects.kmpaletteLoader)
            implementation(libs.compose.ui)
            implementation(libs.kotlinx.coroutines)
        }

        commonTest.dependencies {
            implementation(projects.testUtils)
            implementation(kotlin("test"))
            implementation(libs.compose.ui)
            implementation(libs.kotlinx.coroutines.test)
        }

//        val androidInstrumentedTest by getting {
//            dependencies {
//                implementation(projects.testUtils)
//                implementation(kotlin("test"))
//                implementation(libs.compose.ui)
//                implementation(libs.kotlinx.coroutines.test)
//                implementation(libs.bundles.test.android)
//            }
//        }

        jvmTest.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}