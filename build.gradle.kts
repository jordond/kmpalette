plugins {
    alias(libs.plugins.multiplatform) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.poko) apply false
    alias(libs.plugins.dokka)
    alias(libs.plugins.binaryCompatibility)
    alias(libs.plugins.kover)
}

apiValidation {
    ignoredProjects.addAll(
        listOf(
            "test-utils",
        ),
    )
}

dependencies {
    kover(project(":androidx-palette"))
}


