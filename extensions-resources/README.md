# Compose Multiplatform Resources Extensions

This extension provides `ResourceLoader` a `ImageBitmapLoader` for images using Compose
Multiplatform Resources.

| Library                                                                                                                | Loader           | Input Class | Demo                                                                                                                      |
|------------------------------------------------------------------------------------------------------------------------|------------------|-------------|---------------------------------------------------------------------------------------------------------------------------|
| [Compose Multiplatform Resources](https://github.com/JetBrains/compose-multiplatform/tree/master/components/resources) | `ResourceLoader` | `Resource`  | [`ResourcesPaletteScreen`](../demo/composeApp/src/commonMain/kotlin/com/kmpalette/demo/palette/ResourcesPaletteScreen.kt) |

Each of these extensions provides a `ImageBitmapLoader` object that can be used to generate
an `ImageBitmap` from the input class. For example, the `NetworkLoader` can be used to generate

## Setup

Then you need to add the following to your `build.gradle.kts` file:

```kotlin
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kmpalette.core)
                implementation(libs.kmpalette.extensions.resources)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
            }
        }
    }
}

// ...

android {
    // ...

    sourceSets["main"].apply {
        res.srcDirs("src/androidMain/resources")
        resources.srcDirs("src/commonMain/resources")
    }

    // ...
}
```

**Warning:** Although Compose Resources supports XML and SVG vector images, this library does not.
It will
crash if you attempt to use a vector format.

## Usage

Add your images to the `commonMain/resources` folder. You can then load them using
the `ResourceLoader`:

```kotlin
val imageBitmap: ImageBitmap = ResourceLoader.load(resource("example1.png"))

// Or using one of the composeable functions
val paletteState = rememebrPaletteState(loader = ResourceLoader)
LaunchedEffect(base64Image) {
    paletteState.generate(resource("example1.png"))
}
```