import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.android.application)
}

android {
    namespace = "${libs.versions.group.get()}.android"
    compileSdk =
        libs.versions.sdk.compile
            .get()
            .toInt()

    defaultConfig {
        minSdk =
            libs.versions.sdk.min
                .get()
                .toInt()

        targetSdk = libs.versions.sdk.target
            .get()
            .toInt()

        applicationId = "${libs.versions.group.get()}.android"
        versionCode = 1
        versionName = "1.0.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    compilerOptions { jvmTarget.set(JvmTarget.JVM_17) }
}

dependencies {
    implementation(projects.app.shared)
    implementation(libs.androidx.activityCompose)
}
