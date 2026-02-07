import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(projects.app.shared)
    implementation(libs.filekit.dialogs)

    // Force correct Skiko runtime version to match Compose 1.10.0's Skiko API
    // Fixes: UnsatisfiedLinkError: 'int org.jetbrains.skiko.MetalApiKt.getAdapterMaxTextureSize(long)'
    runtimeOnly("org.jetbrains.skiko:skiko-awt-runtime-macos-arm64:0.9.40")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Kmpalette"
            packageVersion = "1.0.0"

            linux {
                iconFile.set(project.file("appIcons/LinuxIcon.png"))
            }
            windows {
                iconFile.set(project.file("appIcons/WindowsIcon.ico"))
            }
            macOS {
                iconFile.set(project.file("appIcons/MacosIcon.icns"))
                bundleID = "${libs.versions.group.get()}.desktop"
            }
        }
    }
}
