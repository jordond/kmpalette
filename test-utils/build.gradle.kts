import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.android.library)
    alias(libs.plugins.poko)
}

kotlin {
    explicitApi()
    jvmToolchain(jdkVersion = 11)
    applyDefaultHierarchyTemplate()

    android {
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

    js {
        browser()
        binaries.library()
    }

    @Suppress("OPT_IN_USAGE")
    wasmJs {
        browser()
        binaries.library()
    }

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

        create("skikoMain") {
            dependsOn(commonMain.get())
            nativeMain.get().dependsOn(this)
            webMain.get().dependsOn(this)
            jvmMain.get().dependsOn(this)
        }

        jvmTest.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}
