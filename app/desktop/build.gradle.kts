import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(projects.app.shared)
    implementation(libs.filekit.dialogs)

    // Force the correct Skiko runtime version to match Compose 1.10.0's Skiko API
    // Fixes: UnsatisfiedLinkError: 'int org.jetbrains.skiko.MetalApiKt.getAdapterMaxTextureSize(long)'
    //noinspection UseTomlInstead
    runtimeOnly("org.jetbrains.skiko:skiko-awt-runtime-macos-arm64:0.9.47")
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
