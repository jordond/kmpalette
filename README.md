<img width="500px" src="art/logo.png" alt="logo"/>
<br />

![Maven Central](https://img.shields.io/maven-central/v/com.materialkolor.palette/core)
[![Kotlin](https://img.shields.io/badge/kotlin-v2.3.10-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![Build](https://github.com/jordond/kmpalette/actions/workflows/ci.yml/badge.svg)](https://github.com/jordond/kmpalette/actions/workflows/ci.yml)
[![License](https://img.shields.io/github/license/jordond/kmPalette)](https://opensource.org/license/mit/)

[![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-v1.10.0-blue)](https://github.com/JetBrains/compose-multiplatform)
![badge-android](http://img.shields.io/badge/platform-android-6EDB8D.svg?style=flat)
![badge-ios](http://img.shields.io/badge/platform-ios-CDCDCD.svg?style=flat)
![badge-desktop](http://img.shields.io/badge/platform-desktop-DB413D.svg?style=flat)
![badge-js](http://img.shields.io/badge/platform-js-FDD835.svg?style=flat)
![badge-wasm](http://img.shields.io/badge/platform-wasm-FDD835.svg?style=flat)

A Compose Multiplatform library for generating color palettes from images, including the dominant
color. You can use this library in combination
with [MaterialKolor](https://github.com/jordond/materialkolor) to generate dynamic Material
themes based on images.

Supports loading images from several sources, see [sources](#sources).

**Note:** This is a port of
the [`androidx.palette`](https://developer.android.com/jetpack/androidx/releases/palette)
library.

## Table of Contents

- [Platforms](#platforms)
- [Inspiration](#inspiration)
- [Dynamic Material Themes](#dynamic-material-themes)
- [Setup](#setup)
    - [Version Catalog](#version-catalog)
- [Standalone Palette (No Compose)](#standalone-palette-no-compose)
- [Usage](#usage)
    - [Dominant Color](#dominant-color)
    - [Generate a color Palette](#generate-a-color-palette)
    - [Sources](#sources)
- [Migration](#migration)
- [Feature Requests](#feature-requests)
- [Contributing](#contributing)
- [License](#license)
    - [Changes from the original source](#changes-from-the-original-source)

## Platforms

This library is written for Compose Multiplatform and can be used on the following platforms:

| Artifact             | Android | Desktop | iOS | macOS | JS | WASM |
|----------------------|:-------:|:-------:|:---:|:-----:|:--:|:----:|
| `androidx-palette`   |    ✅    |    ✅    |  ✅  |   ✅   | ✅  |  ✅   |
| `core`               |    ✅    |    ✅    |  ✅  |   ✅   | ✅  |  ✅   |
| `extensions-base64`  |    ✅    |    ✅    |  ✅  |   ✅   | ✅  |  ✅   |
| `extensions-network` |    ✅    |    ✅    |  ✅  |   ✅   | ✅  |  ✅   |
| `extensions-file`    |    ✅    |    ✅    |  ✅  |   ✅   | ✅  |  ✅   |

## Inspiration

I created this library because I wanted to use the
[`androidx.palette`](https://developer.android.com/jetpack/androidx/releases/palette) library in a
Compose Multiplatform app. But that library is not multiplatform, so I decided to port it.

## Dynamic Material Themes

Want to create a dynamic Material theme based on the dominant color in an image?

Check out my other Compose Multiplatform
library [MaterialKolor](https://github.com/jordond/materialkolor)!

## Setup

You can add this library to your project using Gradle. There are several optional extension
libraries, see [sources](#sources).

### Version Catalog

In `libs.versions.toml`:

```toml
[versions]
kmpalette = "4.0.0"

[libraries]
kmpalette-core = { module = "com.materialkolor.palette:core", version.ref = "kmpalette" }
# Optional - standalone palette generation without Compose
kmpalette-androidx-palette = { module = "com.materialkolor.palette:androidx-palette", version.ref = "kmpalette" }
# Optional source libraries
kmpalette-extensions-base64 = { module = "com.materialkolor.palette:extensions-base64", version.ref = "kmpalette" }
kmpalette-extensions-network = { module = "com.materialkolor.palette:extensions-network", version.ref = "kmpalette" }
kmpalette-extensions-file = { module = "com.materialkolor.palette:extensions-file", version.ref = "kmpalette" }
```

To add to a multiplatform project, add the dependency to the common source-set:

```kotlin
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                // Core library (includes Compose utilities)
                implementation(libs.kmpalette.core)

                // Or use just the palette generation without Compose
                // implementation(libs.kmpalette.androidx.palette)

                // Optional extensions based on your image source
                implementation(libs.kmpalette.extensions.base64)
                implementation(libs.kmpalette.extensions.network)
                implementation(libs.kmpalette.extensions.file)
            }
        }
    }
}
```

## Standalone Palette (No Compose)

The `androidx-palette` module is a full Kotlin Multiplatform port of
the [`androidx.palette`](https://developer.android.com/jetpack/androidx/releases/palette) library. It
has **no dependency on Compose** and can be used in any Kotlin Multiplatform project.

If you don't need the Compose utilities provided by `core`, you can depend on `androidx-palette`
directly to generate color palettes from raw pixel data:

```kotlin
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kmpalette.androidx.palette)
            }
        }
    }
}
```

```kotlin
val palette = Palette.from(bitmap).generate()
val vibrant = palette.vibrantSwatch
val dominant = palette.dominantSwatch
```

The `core` module includes `androidx-palette` as a transitive dependency, so you don't need to add
both. Use `androidx-palette` on its own when you want palette generation without pulling in Compose.

## Usage

To see the generated KDocs, visit [docs.kmpalette.com](https://docs.kmpalette.com/)

To use this library, you first must have a `ImageBitmap` or a `Painter` object.

To get an `ImageBitmap` you can use one of the [sources](#sources) or by using a library that
creates one for you.

Since this library is a port of
the [`androidx.palette`](https://developer.android.com/jetpack/androidx/releases/palette) library,
the usage is very similar. However, this library provides some helpful extension functions and
composables.

Look in `kmpalette-core` for the main library, including extensions for the `Palette` and `Swatch`
objects.

Included are two helpful `@Composable`-ready State objects:

- `DominantColorState` - A state object that holds a generated dominant `Color` object.
- `PaletteState` - A state object that holds a generated `Palette` object.

They can be used like so:

### Dominant Color

You can generate a dominant color from an `ImageBitmap` using the `rememberDominantColorState`
composable. This will also provide a `onColor` for you to use as a text color.

```kotlin
@Composable
fun SomeComposable(bitmap: ImageBitmap) {
    val dominantColorState = rememberDominantColorState()
    LaunchedEffect(bitmap) {
        dominantColorState.updateFrom(bitmap)
    }

    Box(
        modifier = Modifier
            .width(200.dp)
            .height(100.dp)
            .background(dominantColorState.color)
    ) {
        Text("Some Text", color = dominantColorState.onColor)
    }
}
```

You can also use a `Painter` object with the dedicated `rememberPainterDominantColorState`:

```kotlin
@Composable
fun SomeComposable(painter: Painter) {
    val dominantColorState = rememberPainterDominantColorState()
    LaunchedEffect(painter) {
        dominantColorState.updateFrom(painter)
    }

    // ...
}
```

Since the generation of the dominant color is an asynchronous operation that can fail, you can track
the results of the operation using the `DominantColorState.result` object.

If you want to filter the dominant color, you can use the pass a lambda
to `rememberDominantColorState()`:

```kotlin
val dominantColorState = rememberDominantColorState(
    isSwatchValid = { swatch ->
        swatch.color.contrastAgainst(MaterialTheme.colorScheme.surfaceColor) >= MinContrastRatio
    }
)
LaunchedEffect(bitmap) {
    dominantColorState.updateFrom(bitmap)
}
```

### Generate a color Palette

If you want a whole color palette instead of just a dominant color, you can use
the `rememberPaletteState` composable. This will provide a `Palette` object which contains a few
different color `Swatch`s, each has their own color and _onColor_.

Using an `ImageBitmap`:

```kotlin
@Composable
fun SomeComposable(bitmap: ImageBitmap) {
    val paletteState = rememberPaletteState()
    LaunchedEffect(bitmap) {
        paletteState.generate(bitmap)
    }

    Box(
        modifier = Modifier
            .width(200.dp)
            .height(100.dp)
            .background(paletteState.vibrantSwatch?.color ?: Color.White)
    ) {
        Text(
            text = "Some Text",
            color = paletteState.vibrantSwatch?.onColor ?: LocalContentColor.current,
        )
    }
}
```

Or using a `Painter`:

```kotlin
@Composable
fun SomeComposable(painter: Painter) {
    val paletteState = rememberPainterPaletteState()
    LaunchedEffect(painter) {
        paletteState.generate(painter)
    }

    // ...
}
```

Since the generation of the palette is an asynchronous operation that can fail, you can track
the results of the operation using the `PaletteState.state` object.

### Sources

The `kmpalette-core` library provides the core functionality for generating color palettes from
a `ImageBitmap` or a `Painter` object.

This library provides some extension artifacts for popular sources.

| Artifact                                             | Library                                          | Loader               | Input Class    |
|------------------------------------------------------|--------------------------------------------------|----------------------|----------------|
| [`extensions-base64`](extensions-base64/README.md)   | N/A                                              | `Base64Loader`       | `String`       |
| [`extensions-network`](extensions-network/README.md) | [Ktor](https://github.com/ktorio/ktor)           | `NetworkLoader`      | `Url`          |
| [`extensions-file`](extensions-file/README.md)       | [FileKit](https://github.com/niceplayer/FileKit) | `PlatformFileLoader` | `PlatformFile` |

Each of these extensions provides dedicated composable functions. For example,
the `extensions-network` module provides `rememberNetworkDominantColorState` and
`rememberNetworkPaletteState`:

```kotlin
@Composable
fun SomeComposable(url: Url) {
    val dominantColorState = rememberNetworkDominantColorState(
        defaultColor = MaterialTheme.colorScheme.primary,
        defaultOnColor = MaterialTheme.colorScheme.onPrimary,
    )
    LaunchedEffect(url) {
        dominantColorState.updateFrom(url)
    }

    Box(
        modifier = Modifier
            .width(200.dp)
            .height(100.dp)
            .background(dominantColorState.color)
    ) {
        Text("Some Text", color = dominantColorState.onColor)
    }
}
```

### Compose Multiplatform Resources

To generate a palette from a `DrawableResource` you can use the `rememberResourceDominantColorState`
or `rememberResourcePaletteState` composables which work directly with Compose Multiplatform
resources:

```kotlin
@Composable
fun MyComposable() {
    val dominantColorState = rememberResourceDominantColorState()
    LaunchedEffect(Unit) {
        dominantColorState.updateFrom(Res.drawable.my_image)
    }
}
```

Alternatively, you can use the `@Composable imageResource()` to get an `ImageBitmap` then pass that
to the default loader:

```kotlin
@Composable
fun MyComposable() {
    val image = imageResource(Res.drawable.my_image)
    val dominantColorState = rememberDominantColorState()
    LaunchedEffect(image) {
        dominantColorState.updateFrom(image)
    }
}
```

## Migration

If you are migrating from version 3.x, please see the [Migration Guide](MIGRATION.md) for detailed
instructions on updating your code.

Key changes in 4.0:

- `extensions-bytearray` module has been removed
- `extensions-file` now uses FileKit instead of Okio
- New dedicated composable functions for each loader type

## Feature Requests

If you have a feature request, please open an issue. If you would like to implement a feature
request, refer to the [Contributing](#contributing) section.

## Contributing

Contributions are always welcome!. If you'd like to contribute, please feel free to create a PR or
open an issue.

## License

The module `androidx-palette` is licensed under the Apache License, Version 2.0. See
their [LICENSE](androidx-palette/LICENSE) and their
repository [here](https://github.com/androidx/androidx/tree/androidx-main/palette) for more
information.

### Changes from the original source

- Convert Java code to Kotlin
- Convert library to Kotlin Multiplatform

For the remaining code see [LICENSE](LICENSE) for more information.
