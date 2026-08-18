# androidx-palette

A dependency-free Kotlin Multiplatform port of the
[`androidx.palette`](https://developer.android.com/jetpack/androidx/releases/palette) library.

As of 4.0 this module has **no Compose dependency** (its only dependency is `androidx.annotation`),
so it can be used from any Kotlin Multiplatform project, including ones that don't use Compose at
all.

If you are using Compose Multiplatform, depend on
[`core`](../README.md#setup) instead. It includes this module transitively and adds `ImageBitmap`
support, Compose state objects, and loaders.

## Setup

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("com.materialkolor.palette:androidx-palette:<version>")
        }
    }
}
```

## Platform Support

| Platform | Supported |
|----------|:---------:|
| Android  |     ✅     |
| iOS      |     ✅     |
| macOS    |     ✅¹    |
| Desktop  |     ✅     |
| JS       |     ✅     |
| WASM     |     ✅     |

¹ Apple Silicon only (`macosArm64`). The deprecated `macosX64` target was removed in 4.0.

## Usage

Because the module is Compose-free, it operates on raw ARGB pixel data rather than a bitmap type.
You supply an `IntArray` of pixels plus the source dimensions:

```kotlin
val palette = Palette
    .from(pixels, width, height)
    .maximumColorCount(16)
    .generate()

val vibrant = palette.vibrantSwatch
val dominant = palette.dominantSwatch
```

You can also build a `Palette` from swatches you already have:

```kotlin
val palette = Palette.from(swatches)
```

### Builder options

| Method                                    | Description                                                                        |
|-------------------------------------------|------------------------------------------------------------------------------------|
| `maximumColorCount(colors)`               | Maximum number of colors in the generated palette. Defaults to `16`.                |
| `resizeBitmapArea(area)`                  | Target pixel area to downscale to before quantizing. Defaults to `112 * 112`.       |
| `scaling(enabled)`                        | Enable or disable the built-in nearest-neighbour downscaling.                       |
| `setRegion(left, top, right, bottom)`     | Restrict generation to a region, using plain `Int` coordinates.                     |
| `setRegionCoordinateSpace(width, height)` | Declare the coordinate space a region is expressed in, when the input is pre-scaled.|
| `clearRegion()`                           | Clear a previously set region.                                                      |
| `addFilter(filter)` / `clearFilters()`    | Add or remove a `Palette.Filter`.                                                   |
| `addTarget(target)` / `clearTargets()`    | Add or remove a `Target`.                                                           |

Scaling is pure Kotlin (nearest-neighbour), matching Android's
`Bitmap.createScaledBitmap(filter = false)` behaviour.

If you downscale the pixels yourself before handing them over, turn the builder's scaling off and
tell it the coordinate space your region is in:

```kotlin
Palette.from(scaledPixels, scaledWidth, scaledHeight)
    .scaling(false)
    .setRegionCoordinateSpace(originalWidth, originalHeight)
    .generate()
```

### Reading the palette

```kotlin
// Named swatches
palette.vibrantSwatch
palette.lightVibrantSwatch
palette.darkVibrantSwatch
palette.mutedSwatch
palette.lightMutedSwatch
palette.darkMutedSwatch
palette.dominantSwatch

// All swatches
palette.swatches

// Colors, with a fallback for when the swatch is missing
palette.getVibrantColor(default)
palette.getDominantColor(default)

// By target
palette.getSwatchForTarget(Target.VIBRANT)
palette.getColorForTarget(Target.MUTED, default)
```

### Swatch

| Property         | Description                                                     |
|------------------|-----------------------------------------------------------------|
| `rgb`            | The packed ARGB color.                                          |
| `population`     | Number of pixels represented by this swatch.                    |
| `hsl`            | The color as `FloatArray(3)` of hue, saturation, lightness.     |
| `titleTextColor` | Title text color with guaranteed contrast (min ratio `3.0`).    |
| `bodyTextColor`  | Body text color with guaranteed contrast (min ratio `4.5`).     |

### Targets

Built-in `Target` constants: `VIBRANT`, `LIGHT_VIBRANT`, `DARK_VIBRANT`, `MUTED`, `LIGHT_MUTED`,
`DARK_MUTED`. Build a custom one with `Target.Builder`:

```kotlin
val target = Target.Builder()
    .setMinimumSaturation(0.5f)
    .setTargetLightness(0.6f)
    .setExclusive(false)
    .build()

val palette = Palette.from(pixels, width, height)
    .clearTargets()
    .addTarget(target)
    .generate()
```

### Filters

`Palette.Filter` is a `fun interface`, so a lambda works:

```kotlin
Palette.from(pixels, width, height)
    .clearFilters()
    .addFilter { rgb, hsl -> hsl[2] > 0.2f }
    .generate()
```

`Palette.DEFAULT_FILTER` excludes near-white, near-black, and colors near the red I-line.

## License

This module is licensed under the Apache License, Version 2.0. See [LICENSE](LICENSE) and the
[original repository](https://github.com/androidx/androidx/tree/androidx-main/palette) for more
information.

### Changes from the original source

- Convert Java code to Kotlin
- Convert library to Kotlin Multiplatform
- Remove the Compose and Skiko dependencies, operating on raw `IntArray` pixel data instead
- Replace `Rect`-based region selection with plain `Int` coordinates
- Replace platform-specific bitmap scaling with a pure-Kotlin nearest-neighbour implementation
