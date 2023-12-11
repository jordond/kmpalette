import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import io.gitlab.arturbosch.detekt.report.ReportMergeTask
import org.jetbrains.dokka.gradle.AbstractDokkaTask
import org.jetbrains.dokka.gradle.DokkaMultiModuleTask

plugins {
    alias(libs.plugins.multiplatform) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.cocoapods) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.libres) apply false
    alias(libs.plugins.poko) apply false
    alias(libs.plugins.dokka)
    alias(libs.plugins.dependencies)
    alias(libs.plugins.binaryCompatibility)
    alias(libs.plugins.kover)
    alias(libs.plugins.detekt)
}

apiValidation {
    ignoredProjects.addAll(
        listOf(
            "test-utils",
            "composeApp",
        ),
    )
}

dependencies {
    kover(project(":androidx-palette"))
}

tasks.withType<DokkaMultiModuleTask>().configureEach {
    outputDirectory.set(rootDir.resolve("dokka"))
}

val reportMerge by tasks.registering(ReportMergeTask::class) {
    output.set(rootProject.layout.buildDirectory.file("reports/detekt/detekt.sarif"))
}

// Workaround because Version Catalog isn't available in `allProjects` block.
val catalog = rootProject.libs
allprojects {
    // Configure Detekt
    apply<DetektPlugin>()

    configure<DetektExtension> {
        ignoreFailures = false
        buildUponDefaultConfig = true
        parallel = true
        config.setFrom("$rootDir/detekt-config.yml")
    }

    tasks.withType<Detekt>().configureEach {
        reports {
            sarif.required.set(true)
            html.required.set(false)
            xml.required.set(false)
            txt.required.set(false)
            md.required.set(false)
        }
    }

    // Workaround for https://github.com/Kotlin/dokka/issues/2977.
    // We disable the C Interop IDE metadata task when generating documentation using Dokka.
    tasks.withType<AbstractDokkaTask> {
        @Suppress("UNCHECKED_CAST")
        val taskClass = Class.forName(
            "org.jetbrains.kotlin.gradle.targets.native.internal" +
                ".CInteropMetadataDependencyTransformationTask"
        ) as Class<Task>
        parent?.subprojects?.forEach { project ->
            dependsOn(project.tasks.withType(taskClass))
        }
    }
}

subprojects {
    dependencies {
        detektPlugins(rootProject.libs.detekt.formatting)
        detektPlugins(rootProject.libs.detekt.library)
    }

    tasks.withType<Detekt>().configureEach detekt@{
        exclude("**/demo/**")

        finalizedBy(reportMerge)
        reportMerge.configure {
            input.from(this@detekt.sarifReportFile)
        }
    }
}

tasks.register("detektAll") {
    allprojects {
        this@register.dependsOn(tasks.withType<Detekt>())
    }
}