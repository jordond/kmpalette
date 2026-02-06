import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.poko)
    alias(libs.plugins.dokka)
    alias(libs.plugins.publish)
    alias(libs.plugins.kover)
}

kotlin {
    explicitApi()
    jvmToolchain(jdkVersion = 11)
    applyDefaultHierarchyTemplate()

    androidLibrary {
        namespace = "${libs.versions.group.get()}.palette"
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
            baseName = "androidx-palette"
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.androidx.annotation)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
        }

        @Suppress("unused")
        val skikoMain by creating {
            dependsOn(commonMain.get())
            nativeMain.get().dependsOn(this)
            jvmMain.get().dependsOn(this)
            webMain.get().dependsOn(this)
        }

        androidInstrumentedTest.dependencies {
            implementation(projects.extensionsBase64)
            implementation(kotlin("test"))
            implementation(libs.compose.ui)
            implementation(libs.bundles.test.android)
            implementation(libs.androidx.core)
            implementation(libs.kotlinx.coroutines)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}
