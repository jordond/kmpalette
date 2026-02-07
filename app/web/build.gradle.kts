plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    js {
        browser()
        binaries.executable()
    }

    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.app.shared)
        }
    }
}
