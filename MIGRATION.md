# Migration Guide

This guide helps you migrate from `kmpalette` 3.x to 4.0.

## Overview of Breaking Changes

Version 4.0 includes a significant rewrite of the SDK with the following major changes:

1. **Changed Maven coordinates** - Group changed from `com.kmpalette` to
   `com.materialkolor.palette`, and `kmpalette-core` artifact renamed to `core`
2. **Removed `extensions-bytearray` module**
3. **Replaced Okio with FileKit** in `extensions-file`
4. **Simplified API** – New dedicated composable functions for each loader type

## Quick Migration Checklist

- [ ] Update Maven coordinates (group and artifact IDs)
- [ ] Remove `extensions-bytearray` dependency and any usage
    - The API now supports direct loading via `ByteArray`.
- [ ] Update `extensions-file` usage (Okio → FileKit)
- [ ] Update import statements

---

## Maven Coordinate Changes

The Maven group has changed from `com.kmpalette` to `com.materialkolor.palette`. The core module
artifact has also been renamed from `kmpalette-core` to `core`.

Update all dependency declarations in your `libs.versions.toml` or `build.gradle.kts`:

| Module               | Old Coordinate                     | New Coordinate                                 |
|----------------------|------------------------------------|------------------------------------------------|
| Core                 | `com.kmpalette:kmpalette-core`     | `com.materialkolor.palette:core`               |
| Base64 extension     | `com.kmpalette:extensions-base64`  | `com.materialkolor.palette:extensions-base64`  |
| Network extension    | `com.kmpalette:extensions-network` | `com.materialkolor.palette:extensions-network` |
| File extension       | `com.kmpalette:extensions-file`    | `com.materialkolor.palette:extensions-file`    |
| Palette (no Compose) | `com.kmpalette:androidx-palette`   | `com.materialkolor.palette:androidx-palette`   |

**Note:** Kotlin package names (imports) have **not** changed. Only the Maven coordinates are
different.

---

## Module Changes

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

No additional dependency needed — `ByteArrayLoader` is now included in `core`.

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

### Changed: `extensions-file`

The file extension has been completely rewritten to
use [FileKit](https://github.com/niceplayer/FileKit) instead of Okio.

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

#### API Changes

**Before (3.x) – Using Okio Path:**

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

**Before (3.x) – Using FilePathLoader:**

```kotlin
val paletteState = rememberPaletteState(loader = FilePathLoader)
LaunchedEffect(absolutePath) {
    paletteState.generate(absolutePath) // String path
}
```

**After (4.0) – Android only using java.io.File:**

```kotlin
// Use the new dedicated composable (Android only)
val paletteState = rememberFilePaletteState()
LaunchedEffect(file) {
    paletteState.generate(file)
}
```

---

## New Simplified API

Version 4.0 introduces dedicated composable functions for each loader type, eliminating the need to
manually create and pass loaders.

### Core Module (`kmpalette-core`)

#### ImageBitmap (unchanged)

```kotlin
// Still works the same
val paletteState = rememberPaletteState()
LaunchedEffect(bitmap) {
    paletteState.generate(bitmap)
}
```

#### Painter

**Before (3.x):**

```kotlin
val loader = rememberPainterLoader()
val paletteState = rememberPaletteState(loader = loader)
LaunchedEffect(painter) {
    paletteState.generate(painter)
}
```

**After (4.0):**

```kotlin
// New dedicated function
val paletteState = rememberPainterPaletteState()
LaunchedEffect(painter) {
    paletteState.generate(painter)
}

// Or for dominant color
val dominantColorState = rememberPainterDominantColorState()
LaunchedEffect(painter) {
    dominantColorState.updateFrom(painter)
}
```

#### DrawableResource (Compose Multiplatform Resources)

**Before (3.x):**

```kotlin
// Required manual conversion
val image = imageResource(Res.drawable.my_image)
val paletteState = rememberPaletteState()
LaunchedEffect(image) {
    paletteState.generate(image)
}
```

**After (4.0):**

```kotlin
// New dedicated function - works directly with DrawableResource
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

## Import Changes

### Removed Imports

```kotlin
// No longer available
import com.kmpalette.loader.ByteArrayLoader
import com.kmpalette.loader.FilePathLoader
import com.kmpalette.loader.PathLoader
import com.kmpalette.extensions.bytearray.*
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
kmpalette-extensions-network = { module = "com.kmpalette:extensions-network", version.ref = "kmpalette" }
kmpalette-extensions-file = { module = "com.kmpalette:extensions-file", version.ref = "kmpalette" }
```

**After (4.0):**

```toml
[versions]
kmpalette = "4.0.0"

[libraries]
kmpalette-core = { module = "com.materialkolor.palette:core", version.ref = "kmpalette" }
kmpalette-extensions-base64 = { module = "com.materialkolor.palette:extensions-base64", version.ref = "kmpalette" }
# REMOVED: kmpalette-extensions-bytearray
kmpalette-extensions-network = { module = "com.materialkolor.palette:extensions-network", version.ref = "kmpalette" }
kmpalette-extensions-file = { module = "com.materialkolor.palette:extensions-file", version.ref = "kmpalette" }
androidx-palette = { module = "com.materialkolor.palette:androidx-palette", version.ref = "kmpalette" }
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

**Note:** `extensions-bytearray` has been removed from all platforms.

---

## Need Help?

If you encounter any issues during migration, please:

1. Check the [README](README.md) for updated documentation
2. Review the module-specific README for detailed API documentation
3. [Open an issue](https://github.com/jordond/kmpalette/issues) on GitHub
