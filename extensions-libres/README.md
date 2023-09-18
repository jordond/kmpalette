# Libres Extensions

This extension provides `LibresLoader` a `ImageBitmapLoader` for
loading [Libres](https://github.com/Skeptick/libres) `Image` objects.

| Library                                      | Loader         | Input Class | Demo                                                                                                                |
|----------------------------------------------|----------------|-------------|---------------------------------------------------------------------------------------------------------------------|
| [libres](https://github.com/Skeptick/libres) | `LibresLoader` | `Image`     | [`LibresPaletteScreen`](../demo/composeApp/src/commonMain/kotlin/com/kmpalette/demo/palette/LibresPaletteScreen.kt) |

## Setup

First you need to follow the setup instructions for [Libres](https://github.com/Skeptick/libres).

Then you need to add the following to your `build.gradle.kts` file:

```kotlin
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kmpalette.core)
                implementation(libs.kmpalette.extensions.libres)
            }
        }
    }
}
```

**Warning:** Although Libres supports XML and SVG vector images, this library does not. It will
crash if you attempt to use a vector format.

## Usage

Add your images to the `libres/images` folder in the `commonMain` source set. You can then load them
using the `LibresLoader`:

```kotlin
val imageBitmap: ImageBitmap = LibresLoader.load(Res.image.example1)

// Or using one of the composeable functions
val paletteState = rememebrPaletteState(loader = Base64Loader)
LaunchedEffect(base64Image) {
    paletteState.generate(Res.image.example1)
}
```