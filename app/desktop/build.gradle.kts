import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(projects.app.shared)
    implementation(libs.filekit.dialogs)
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "KMPalette"
            packageVersion = "1.0.0"

            macOS {
                bundleID = "${libs.versions.group.get()}.desktop"
            }
        }
    }
}
