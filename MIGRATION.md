# Migration Guide

This guide helps you migrate from `kmpalette` 3.x to 4.0.

## Overview of Breaking Changes

Version 4.0 is a significant rewrite of the library:

1. **Changed Maven coordinates.** Group changed from `com.kmpalette` to
   `com.materialkolor.palette`, and the `kmpalette-core` artifact was renamed to `core`
2. **`androidx-palette` no longer depends on Compose.** `Palette.Builder` now takes raw
   `IntArray` pixel data instead of an `ImageBitmap`
3. **Renamed `kmpalette-bitmap-loader` to `kmpalette-loader`**
4. **Removed `extensions-bytearray` module.** `ByteArrayLoader` moved into `core`
5. **Removed `extensions-resources` module.** Folded into `core`, package renamed
6. **Removed `extensions-libres` module.** No replacement
7. **Replaced Okio with FileKit** in `extensions-file`
8. **`PaletteResult.Error.cause` is now an `Exception`,** was a `Throwable`
9. **Simplified API** with dedicated composable functions for each loader type
10. **Painter composables moved** to `com.kmpalette.extensions.painter`
11. **Removed the `macosX64` and `iosX64` targets.** Apple Silicon only
12. **Dropped the Material 3 dependency.** `defaultColor` / `defaultOnColor` are now always required
13. **One `DEFAULT_CACHE_SIZE` constant** replaces `DominantColorState.DEFAULT_CACHE_SIZE` and
    `PaletteState.DEFAULT_CACHE_SIZE`

## Quick Migration Checklist

- [ ] Update Maven coordinates (group and artifact IDs)
- [ ] If you used `androidx-palette` directly, switch from `Palette.from(bitmap)` to
      `Palette.from(pixels, width, height)`, or depend on `core` and use its `ImageBitmap` overload
- [ ] Replace `Rect`-based `setRegion` calls with the `Int` overload
- [ ] Remove `extensions-bytearray` dependency and any usage
    - `ByteArrayLoader` is now in `core`.
- [ ] Remove `extensions-resources` dependency; the composables are in `core` now
- [ ] Replace `extensions-libres` usage; there is no Libres loader in 4.0
- [ ] Rename `kmpalette-bitmap-loader` to `kmpalette-loader` if you declared it
- [ ] Update `extensions-file` usage (Okio → FileKit)
- [ ] Pass `defaultColor` / `defaultOnColor` to the dedicated dominant-color composables
- [ ] Drop `macosX64` and `iosX64` from your own targets if you were building against them
- [ ] Change `PaletteResult.Error` handling from `Throwable` to `Exception`
- [ ] Pass `defaultColor` / `defaultOnColor` to `rememberDominantColorState`
- [ ] Replace `DominantColorState.DEFAULT_CACHE_SIZE` / `PaletteState.DEFAULT_CACHE_SIZE` with
      `DEFAULT_CACHE_SIZE`
- [ ] Update painter imports to `com.kmpalette.extensions.painter`
- [ ] Update import statements

---

## Maven Coordinate Changes

The Maven group has changed from `com.kmpalette` to `com.materialkolor.palette`. The core module
artifact has also been renamed from `kmpalette-core` to `core`.

Update all dependency declarations in your `libs.versions.toml` or `build.gradle.kts`:

| Module               | Old Coordinate                     | New Coordinate                                  |
|----------------------|------------------------------------|-------------------------------------------------|
| Core                 | `com.kmpalette:kmpalette-core`     | `com.materialkolor.palette:core`                |
| Base64 extension     | `com.kmpalette:extensions-base64`  | `com.materialkolor.palette:extensions-base64`   |
| Network extension    | `com.kmpalette:extensions-network` | `com.materialkolor.palette:extensions-network`  |
| File extension       | `com.kmpalette:extensions-file`    | `com.materialkolor.palette:extensions-file`     |
| Palette (no Compose) | `com.kmpalette:androidx-palette`   | `com.materialkolor.palette:androidx-palette`    |
| Loader interface     | `com.kmpalette:kmpalette-bitmap-loader` | `com.materialkolor.palette:kmpalette-loader` |
| Resources extension  | `com.kmpalette:extensions-resources` | Removed, now part of `core`                   |
| Libres extension     | `com.kmpalette:extensions-libres`  | Removed, no replacement                         |
| ByteArray extension  | `com.kmpalette:extensions-bytearray` | Removed, now part of `core`                   |

**Note:** Kotlin package names (imports) have **not** changed. Only the Maven coordinates are
different.

---

## Module Changes

### Rewritten: `androidx-palette` is now Compose-free

The biggest change in 4.0. The `androidx-palette` module was rewritten to have no Compose (or
Skiko) dependency at all; its only remaining dependency is `androidx.annotation`. This makes it
usable from any Kotlin Multiplatform project, including ones that don't use Compose.

The consequence is that `Palette.Builder` no longer knows what an `ImageBitmap` is. It operates on
raw ARGB pixel data.

**Before (3.x):**

```kotlin
val palette = Palette.from(imageBitmap).generate()
```

**After (4.0), using `androidx-palette` directly:**

```kotlin
val pixels = IntArray(bitmap.width * bitmap.height)
bitmap.readPixels(pixels)

val palette = Palette.from(pixels, bitmap.width, bitmap.height).generate()
```

**After (4.0), using `core`, which adds an `ImageBitmap` overload:**

```kotlin
// core provides Palette.Companion.from(ImageBitmap), which extracts and
// downscales the pixels for you
val palette = Palette.from(bitmap).generate()
```

If you already depend on `core`, the call site is unchanged, because `core` re-adds the `ImageBitmap`
entry point as an extension.

#### Builder API changes

| 3.x                                       | 4.0                                                    |
|-------------------------------------------|--------------------------------------------------------|
| `Palette.from(bitmap: ImageBitmap)`       | `Palette.from(pixels: IntArray, width: Int, height: Int)` |
| `setRegion(rect: Rect)`                   | `setRegion(left: Int, top: Int, right: Int, bottom: Int)` |
| Platform-specific bitmap scaling          | Pure-Kotlin nearest-neighbour scaling                  |
| N/A                                       | `scaling(enabled: Boolean)` to opt out of downscaling  |
| N/A                                       | `setRegionCoordinateSpace(width, height)` for pre-scaled input |

`resizeBitmapArea(area)` still exists and still defaults to `Palette.DEFAULT_RESIZE_BITMAP_AREA`
(`112 * 112`).

If you pre-scale the pixels yourself, disable the builder's scaling and tell it what coordinate
space your region is expressed in:

```kotlin
Palette.from(scaledPixels, scaledWidth, scaledHeight)
    .scaling(false)
    .setRegionCoordinateSpace(originalWidth, originalHeight)
    .generate()
```

---

### Renamed: `kmpalette-bitmap-loader` to `kmpalette-loader`

The artifact holding `ImageBitmapLoader<T>` was renamed from `kmpalette-bitmap-loader` to
`kmpalette-loader`. The package (`com.kmpalette.loader`) and the interface are unchanged, so only
the dependency coordinate needs updating.

```diff
- implementation("com.kmpalette:kmpalette-bitmap-loader:3.x.x")
+ implementation("com.materialkolor.palette:kmpalette-loader:4.x.x")
```

`core` and every `extensions-*` artifact expose it with `api`, so you only need to declare it
explicitly if you're writing a loader in a module that doesn't otherwise depend on `core`.

---

### Removed: `extensions-bytearray`

The `extensions-bytearray` module has been removed. ByteArray support is now built into the `core`
module.

**Before (3.x):**

```kotlin
// build.gradle.kts
implementation("com.kmpalette:extensions-bytearray:3.x.x")

// Usage
val loader = ByteArrayLoader
val paletteState = rememberPaletteState(loader = loader)
LaunchedEffect(byteArray) {
    paletteState.generate(byteArray)
}
```

**After (4.0):**

No additional dependency needed. `ByteArrayLoader` is now included in `core`.

```kotlin
// Use the built-in ByteArrayLoader with the generic composable
val paletteState = rememberPaletteState(loader = ByteArrayLoader)
LaunchedEffect(byteArray) {
    paletteState.generate(byteArray)
}

// Or for dominant color
val dominantColorState = rememberDominantColorState(loader = ByteArrayLoader)
LaunchedEffect(byteArray) {
    dominantColorState.updateFrom(byteArray)
}
```

---

### Removed: `extensions-resources`

The module is gone. Its composables now ship in `core`, and they work on `DrawableResource` instead
of the experimental `Resource` type.

```diff
- implementation("com.kmpalette:extensions-resources:3.x.x")
```

The package lost its trailing `s`, and the dominant-color function was renamed from
`rememberResourcesDominantColorState` to `rememberResourceDominantColorState`:

```diff
- import com.kmpalette.extensions.resources.rememberResourcesDominantColorState
- import com.kmpalette.extensions.resources.rememberResourcePaletteState
+ import com.kmpalette.extensions.resource.rememberResourceDominantColorState
+ import com.kmpalette.extensions.resource.rememberResourcePaletteState
```

`ResourceLoader` stays in `com.kmpalette.loader`, but now lives in `core` and takes a
`ResourceEnvironment`. Use `rememberResourceLoader()` to get one.

---

### Removed: `extensions-libres`

The [Libres](https://github.com/Skeptick/libres) loader has been removed with no replacement.

```diff
- implementation("com.kmpalette:extensions-libres:3.x.x")
```

`LibresLoader`, `rememberLibresDominantColorState` and `rememberLibresPaletteState` are all gone. If
you use Libres, resolve the image to an `ImageBitmap` or `ByteArray` yourself and pass it to
`rememberPaletteState` or `ByteArrayLoader`.

---

### Changed: `extensions-file`

The file extension has been completely rewritten to
use [FileKit](https://github.com/vinceglb/FileKit) instead of Okio.

#### Dependency Changes

**Before (3.x):**

```kotlin
// build.gradle.kts
implementation("com.kmpalette:extensions-file:3.x.x")
implementation("com.squareup.okio:okio:x.x.x")
```

**After (4.0):**

```kotlin
// build.gradle.kts
implementation("com.materialkolor.palette:extensions-file:4.x.x")
implementation("io.github.vinceglb:filekit-core:<version>")
```

FileKit is a `implementation` dependency of `extensions-file`, so it is **not** exposed
transitively, so add it yourself.

#### API Changes

**Before (3.x), using Okio `Path`:**

```kotlin
val paletteState = rememberPaletteState(loader = PathLoader)
LaunchedEffect(path) {
    paletteState.generate(path)
}
```

**After (4.0) - Using PlatformFile:**

```kotlin
// Use the new dedicated composable
val paletteState = rememberPlatformFilePaletteState()
LaunchedEffect(platformFile) {
    paletteState.generate(platformFile)
}

// Or for dominant color
val dominantColorState = rememberPlatformFileDominantColorState(
    defaultColor = Color.Black,
    defaultOnColor = Color.White,
)
LaunchedEffect(platformFile) {
    dominantColorState.updateFrom(platformFile)
}
```

**Before (3.x), using `FilePathLoader`:**

```kotlin
val paletteState = rememberPaletteState(loader = FilePathLoader)
LaunchedEffect(absolutePath) {
    paletteState.generate(absolutePath) // String path
}
```

**After (4.0), Android only, using `java.io.File`:**

```kotlin
// Use the new dedicated composable (Android only)
val paletteState = rememberFilePaletteState()
LaunchedEffect(file) {
    paletteState.generate(file)
}
```

---

## Removed: the Material 3 fallback

`rememberDominantColorState` used to default `defaultColor` / `defaultOnColor` to
`MaterialTheme.colorScheme.primary` / `onPrimary`. That silently produced the baseline Material 3
palette for anyone not using a Material 3 theme, and forced a `compose.material3` dependency on
`core`. Both parameters are now required everywhere, and the library no longer depends on
Material 3.

**Before:**

```kotlin
val state = rememberDominantColorState()
```

**After:**

```kotlin
val state = rememberDominantColorState(
    defaultColor = MaterialTheme.colorScheme.primary,
    defaultOnColor = MaterialTheme.colorScheme.onPrimary,
)
```

Pass whatever your design system uses; nothing assumes Material 3.

---

## Changed: a single `DEFAULT_CACHE_SIZE`

`DominantColorState.DEFAULT_CACHE_SIZE` (5) and `PaletteState.DEFAULT_CACHE_SIZE` (6) have been
replaced by one top-level constant, and both companion objects are gone.

```kotlin
import com.kmpalette.DEFAULT_CACHE_SIZE   // 6
```

Every state function now defaults to it. Previously `rememberPaletteState()` referenced the
`DominantColorState` constant while `rememberBase64PaletteState()` used the `PaletteState` one, so
the two cached different amounts, and `rememberDominantColorState()` disabled its cache entirely.

---

## Changed: `PaletteResult`

`PaletteResult` already existed in 3.x with the same shape. The only breaking change is that
`Error.cause` is now an `Exception` rather than a `Throwable`:

```diff
- public data class Error(val cause: Throwable) : PaletteResult
+ public data class Error(val cause: Exception) : PaletteResult
```

For reference, the full type:

```kotlin
public sealed interface PaletteResult {
    public data class Success(val palette: Palette) : PaletteResult
    public data object Loading : PaletteResult
    public data class Error(val cause: Exception) : PaletteResult

    public val paletteOrNull: Palette?
}
```

- `PaletteState.state` is a `PaletteResult?` (null before the first `generate()` call)
- `PaletteState.palette` is a convenience shortcut for the success case
- `DominantColorState.result` is a `PaletteResult?`

```kotlin
when (val state = paletteState.state) {
    null -> { /* nothing generated yet */ }
    PaletteResult.Loading -> ShowSpinner()
    is PaletteResult.Error -> ShowError(state.cause)
    is PaletteResult.Success -> ShowPalette(state.palette)
}
```

`PaletteState` exposes the palette via the `palette` property. Swatches are reached through it, not
directly on the state object:

```kotlin
val vibrant = paletteState.palette?.vibrantSwatch
```

---

## New: `SwatchTarget`

`core` adds a Kotlin sealed interface over `androidx-palette`'s `Target`, with operator access:

```kotlin
val vibrant = palette[SwatchTarget.Vibrant]
val darkMuted = palette.getSwatch(SwatchTarget.MutedDark)
```

Targets available: `Vibrant`, `VibrantDark`, `VibrantLight`, `Muted`, `MutedDark`, `MutedLight`.
The raw `Target` type still works: `palette[Target.VIBRANT]`.

---

## New Simplified API

Version 4.0 introduces dedicated composable functions for each loader type, eliminating the need to
manually create and pass loaders.

> **Note:** every dominant-color composable requires `defaultColor` and `defaultOnColor`. The
> palette-state equivalents take no required parameters.

### Core Module (`core`)

#### ImageBitmap (unchanged)

```kotlin
// Still works the same
val paletteState = rememberPaletteState()
LaunchedEffect(bitmap) {
    paletteState.generate(bitmap)
}
```

#### Painter

`rememberPainterDominantColorState` and `rememberPainterPaletteState` already existed in 3.x. They
have **moved package**, from `com.kmpalette` to `com.kmpalette.extensions.painter`, so they sit
alongside the resource and drawable composables. Update the import:

```diff
- import com.kmpalette.rememberPainterDominantColorState
- import com.kmpalette.rememberPainterPaletteState
+ import com.kmpalette.extensions.painter.rememberPainterDominantColorState
+ import com.kmpalette.extensions.painter.rememberPainterPaletteState
```

`rememberPainterPaletteState()` is otherwise unchanged:

```kotlin
val paletteState = rememberPainterPaletteState()
LaunchedEffect(painter) {
    paletteState.generate(painter)
}
```

`rememberPainterDominantColorState` additionally requires the default colors, which used to fall
back to the Material color scheme:

```kotlin
val dominantColorState = rememberPainterDominantColorState(
    defaultColor = Color.Black,
    defaultOnColor = Color.White,
)
LaunchedEffect(painter) {
    dominantColorState.updateFrom(painter)
}
```

The 3.x approach of building the loader yourself still works:

```kotlin
val paletteState = rememberPaletteState(loader = rememberPainterLoader())
```

**Note:** the 3.x `rememberPainterPaletteState` rebuilt its `PainterLoader` on every recomposition,
which reset the state object each pass. The version in `extensions.painter` does not.

#### DrawableResource (Compose Multiplatform Resources)

In 3.x these lived in the separate `extensions-resources` artifact and worked on the experimental
`Resource` type. See [Removed: `extensions-resources`](#removed-extensions-resources) below.

**Before (3.x):**

```kotlin
// implementation("com.kmpalette:extensions-resources:3.x.x")
import com.kmpalette.extensions.resources.rememberResourcePaletteState

val paletteState = rememberResourcePaletteState()
LaunchedEffect(resource) {
    paletteState.generate(resource) // Resource
}
```

**After (4.0):**

```kotlin
// no extra dependency, and it takes a DrawableResource
val paletteState = rememberResourcePaletteState()
LaunchedEffect(Res.drawable.my_image) {
    paletteState.generate(Res.drawable.my_image)
}

// Or for dominant color
val dominantColorState = rememberResourceDominantColorState(
    defaultColor = MaterialTheme.colorScheme.primary,
    defaultOnColor = MaterialTheme.colorScheme.onPrimary,
)
LaunchedEffect(Res.drawable.my_image) {
    dominantColorState.updateFrom(Res.drawable.my_image)
}
```

#### Android drawable resources

New in 4.0, `core` provides an Android-only loader for `@DrawableRes` ids:

```kotlin
val paletteState = rememberDrawablePaletteState()
LaunchedEffect(R.drawable.my_image) {
    paletteState.generate(R.drawable.my_image)
}
```

### Extension Modules

#### Base64 (`extensions-base64`)

**Before (3.x):**

```kotlin
val paletteState = rememberPaletteState(loader = Base64Loader)
LaunchedEffect(base64String) {
    paletteState.generate(base64String)
}
```

**After (4.0):**

```kotlin
// New dedicated function
val paletteState = rememberBase64PaletteState()
LaunchedEffect(base64String) {
    paletteState.generate(base64String)
}

// Or for dominant color
val dominantColorState = rememberBase64DominantColorState(
    defaultColor = Color.Black,
    defaultOnColor = Color.White,
)
LaunchedEffect(base64String) {
    dominantColorState.updateFrom(base64String)
}
```

#### Network (`extensions-network`)

**Before (3.x):**

```kotlin
val networkLoader = rememberNetworkLoader()
val paletteState = rememberPaletteState(loader = networkLoader)
LaunchedEffect(url) {
    paletteState.generate(url)
}
```

**After (4.0):**

```kotlin
// New dedicated function
val paletteState = rememberNetworkPaletteState()
LaunchedEffect(url) {
    paletteState.generate(url)
}

// Or for dominant color with custom HttpClient
val dominantColorState = rememberNetworkDominantColorState(
    defaultColor = Color.Black,
    defaultOnColor = Color.White,
    httpClient = myHttpClient,
)
LaunchedEffect(url) {
    dominantColorState.updateFrom(url)
}
```

---

## One-shot Generation

New in 4.0. If you don't need a state object, generate a palette directly:

```kotlin
// From a coroutine
val palette = bitmap.generatePalette()

// From a composable
val result: PaletteResult = bitmap.rememberGeneratePalette()

// With a suspending loader
val result = rememberGeneratePalette(loader = { ByteArrayLoader.load(bytes) })

// From a Url (extensions-network)
val result = url.rememberGeneratePalette()
```

---

## Import Changes

### Removed Imports

```kotlin
// No longer available
import com.kmpalette.loader.FilePathLoader
import com.kmpalette.loader.PathLoader
import com.kmpalette.extensions.bytearray.*
import com.kmpalette.extensions.libres.*
import com.kmpalette.loader.LibresLoader
```

### Moved and Renamed Imports

```diff
- import com.kmpalette.rememberPainterDominantColorState
- import com.kmpalette.rememberPainterPaletteState
+ import com.kmpalette.extensions.painter.rememberPainterDominantColorState
+ import com.kmpalette.extensions.painter.rememberPainterPaletteState

- import com.kmpalette.extensions.resources.rememberResourcesDominantColorState
- import com.kmpalette.extensions.resources.rememberResourcePaletteState
+ import com.kmpalette.extensions.resource.rememberResourceDominantColorState
+ import com.kmpalette.extensions.resource.rememberResourcePaletteState
```

`com.kmpalette.loader.ByteArrayLoader` still exists. It moved from `extensions-bytearray` into
`core`, keeping the same package.

### Removed Constants

`DominantColorState.DEFAULT_CACHE_SIZE` and `PaletteState.DEFAULT_CACHE_SIZE` are gone, along with
both companion objects. Use the top-level constant instead:

```diff
- cacheSize = DominantColorState.DEFAULT_CACHE_SIZE
- cacheSize = PaletteState.DEFAULT_CACHE_SIZE
+ import com.kmpalette.DEFAULT_CACHE_SIZE
+ cacheSize = DEFAULT_CACHE_SIZE
```

---

## Gradle Dependencies Update

**Before (3.x):**

```toml
[versions]
kmpalette = "3.1.0"

[libraries]
kmpalette-core = { module = "com.kmpalette:kmpalette-core", version.ref = "kmpalette" }
kmpalette-extensions-base64 = { module = "com.kmpalette:extensions-base64", version.ref = "kmpalette" }
kmpalette-extensions-bytearray = { module = "com.kmpalette:extensions-bytearray", version.ref = "kmpalette" }
kmpalette-extensions-resources = { module = "com.kmpalette:extensions-resources", version.ref = "kmpalette" }
kmpalette-extensions-libres = { module = "com.kmpalette:extensions-libres", version.ref = "kmpalette" }
kmpalette-extensions-network = { module = "com.kmpalette:extensions-network", version.ref = "kmpalette" }
kmpalette-extensions-file = { module = "com.kmpalette:extensions-file", version.ref = "kmpalette" }
kmpalette-bitmap-loader = { module = "com.kmpalette:kmpalette-bitmap-loader", version.ref = "kmpalette" }
```

**After (4.0):**

```toml
[versions]
kmpalette = "4.0.0"

[libraries]
kmpalette-core = { module = "com.materialkolor.palette:core", version.ref = "kmpalette" }
kmpalette-extensions-base64 = { module = "com.materialkolor.palette:extensions-base64", version.ref = "kmpalette" }
# REMOVED: kmpalette-extensions-bytearray (ByteArrayLoader is in core)
# REMOVED: kmpalette-extensions-resources (now part of core)
# REMOVED: kmpalette-extensions-libres (no replacement)
kmpalette-extensions-network = { module = "com.materialkolor.palette:extensions-network", version.ref = "kmpalette" }
kmpalette-extensions-file = { module = "com.materialkolor.palette:extensions-file", version.ref = "kmpalette" }
androidx-palette = { module = "com.materialkolor.palette:androidx-palette", version.ref = "kmpalette" }
# Renamed from kmpalette-bitmap-loader. Only needed if you implement
# ImageBitmapLoader without depending on core
kmpalette-loader = { module = "com.materialkolor.palette:kmpalette-loader", version.ref = "kmpalette" }
```

---

## Platform Support Changes

| Artifact             | Android | Desktop | iOS | macOS | Browser (JS/WASM) |
|----------------------|:-------:|:-------:|:---:|:-----:|:-----------------:|
| `core`               |    ✅    |    ✅    |  ✅  |   ✅   |         ✅         |
| `extensions-base64`  |    ✅    |    ✅    |  ✅  |   ✅   |         ✅         |
| `extensions-network` |    ✅    |    ✅    |  ✅  |   ✅   |         ✅         |
| `extensions-file`    |    ✅    |    ✅    |  ✅  |   ✅   |         ✅         |
| `androidx-palette`   |    ✅    |    ✅    |  ✅  |   ✅   |         ✅         |
| `kmpalette-loader`   |    ✅    |    ✅    |  ✅  |   ✅   |         ✅         |

**Note:** `extensions-bytearray`, `extensions-resources` and `extensions-libres` have been removed
from all platforms. `kmpalette-bitmap-loader` was renamed to `kmpalette-loader`.

### Removed: `macosX64` and `iosX64`

Both Intel targets have been **removed from every artifact**. 4.0 publishes Apple Silicon only.

| Target              | 3.x | 4.0 |
|---------------------|:---:|:---:|
| `macosArm64`        |  ✅  |  ✅  |
| `macosX64`          |  ✅  |  ❌  |
| `iosArm64`          |  ✅  |  ✅  |
| `iosSimulatorArm64` |  ✅  |  ✅  |
| `iosX64`            |  ✅  |  ❌  |

`iosX64` is the Intel iOS simulator, so this only affects Intel Macs. If your project declares
either target, remove it; there is no 4.0 artifact to resolve against.

---

## Need Help?

If you encounter any issues during migration, please:

1. Check the [README](README.md) for updated documentation
2. Review the module-specific README for detailed API documentation
3. [Open an issue](https://github.com/jordond/kmpalette/issues) on GitHub
