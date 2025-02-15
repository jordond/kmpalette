import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.cocoapods)
    alias(libs.plugins.android.application)
}

kotlin {
    applyDefaultHierarchyTemplate()

    androidTarget()

    jvm("desktop")

    js(IR) {
        browser()
        binaries.executable()
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        version = "1.0.0"
        summary = "Compose application framework"
        homepage = "empty"
        ios.deploymentTarget = "11.0"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        all {
            languageSettings {
                optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
                optIn("androidx.compose.foundation.layout.ExperimentalLayoutApi")
                optIn("androidx.compose.material3.ExperimentalMaterial3Api")
                optIn("kotlin.ExperimentalStdlibApi")
            }
        }

        val commonMain by getting {
            dependencies {
                implementation(project(":kmpalette-core"))
                implementation(project(":extensions-base64"))
                implementation(project(":extensions-network"))
                implementation(compose.runtime)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
                implementation(libs.kotlinx.coroutines)
                implementation(libs.materialKolor)
                implementation(libs.voyager.navigator)
                implementation(libs.kermit)
                implementation(libs.calf.filePicker)
                implementation(libs.ktor.client)
                implementation(libs.kamel)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(project(":extensions-file"))
                implementation(libs.androidx.appcompat)
                implementation(libs.androidx.activityCompose)
                implementation(libs.compose.uitooling)
                implementation(libs.kotlinx.coroutines.android)
                implementation(libs.ktor.android)
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.common)
                implementation(compose.desktop.currentOs)
                implementation(libs.ktor.okhttp)
            }
        }

        val iosMain by getting {
            dependencies {
                implementation(libs.ktor.darwin)
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(compose.html.core)
                implementation(libs.ktor.js)
            }
        }
    }
}

android {
    namespace = "com.kmpalette.demo"
    compileSdk = libs.versions.sdk.compile.get().toInt()

    defaultConfig {
        minSdk = 24
        targetSdk = libs.versions.sdk.target.get().toInt()

        applicationId = "com.kmpalette.demo"
        versionCode = 1
        versionName = "1.0.0"
    }

    sourceSets["main"].apply {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
        res.srcDirs("src/androidMain/composeResources")
        resources.srcDirs("src/commonMain/composeResources")
    }

    kotlin {
        jvmToolchain(11)
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.kmpalette.desktopApp"
            packageVersion = "1.0.0"
        }
    }
}