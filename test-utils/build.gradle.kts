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
        namespace = "${libs.versions.group.get()}.test"
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
            baseName = "test-utils"
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.kmpaletteCore)
            implementation(libs.compose.resources)
            implementation(libs.compose.ui)
            implementation(libs.kotlinx.coroutines)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.compose.ui)
            implementation(libs.kotlinx.coroutines.test)
        }

        @Suppress("unused")
        val skikoMain by creating {
            dependsOn(commonMain.get())
            nativeMain.get().dependsOn(this)
            webMain.get().dependsOn(this)
            jvmMain.get().dependsOn(this)
        }

//        val androidInstrumentedTest by getting {
//            dependencies {
//                implementation(kotlin("test"))
//                implementation(compose.ui)
//                implementation(libs.kotlinx.coroutines.test)
//                implementation(libs.bundles.test.android)
//            }
//        }

        jvmTest.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}