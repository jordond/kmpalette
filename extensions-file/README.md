# File Extensions

This extension provides loaders for loading images from files using
[FileKit](https://filekit.mintlify.app/introduction).

| Platform | Loader               | Input Class    |
|----------|----------------------|----------------|
| All      | `PlatformFileLoader` | `PlatformFile` |
| Android  | `FileLoader`         | `File`         |

## Setup

Add the following to your `build.gradle.kts` file:

```kotlin
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kmpalette.core)
                implementation(libs.kmpalette.extensions.file)
                implementation("io.github.vinceglb:filekit-core:<version>")
            }
        }
    }
}
```

## Platform Support

| Platform | Supported |
|----------|:---------:|
| Android  |     ✅     |
| iOS      |     ✅     |
| macOS    |     ✅     |
| Desktop  |     ✅     |
| JS       |     ✅     |
| WASM     |     ✅     |

## Usage

### PlatformFile (All Platforms)

Use `rememberPlatformFileDominantColorState` or `rememberPlatformFilePaletteState` to work with
FileKit's `PlatformFile`:

```kotlin
@Composable
fun MyComposable(file: PlatformFile) {
    val dominantColorState = rememberPlatformFileDominantColorState()
    LaunchedEffect(file) {
        dominantColorState.updateFrom(file)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(dominantColorState.color)
    ) {
        Text("Hello", color = dominantColorState.onColor)
    }
}
```

#### Palette Generation

```kotlin
@Composable
fun MyComposable(file: PlatformFile) {
    val paletteState = rememberPlatformFilePaletteState()

    LaunchedEffect(file) {
        paletteState.generate(file)
    }

    val vibrant = paletteState.palette?.vibrantSwatch

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(vibrant?.color ?: Color.White)
    ) {
        Text("Hello", color = vibrant?.onColor ?: Color.Black)
    }
}
```

### java.io.File (Android Only)

On Android, you can also use `java.io.File` directly with the dedicated composables:

```kotlin
@Composable
fun MyComposable(file: File) {
    val dominantColorState = rememberFileDominantColorState(
        defaultColor = Color.Black,
        defaultOnColor = Color.White,
    )

    LaunchedEffect(file) {
        dominantColorState.updateFrom(file)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(dominantColorState.color)
    ) {
        Text("Hello", color = dominantColorState.onColor)
    }
}
```

#### Palette Generation (Android)

```kotlin
@Composable
fun MyComposable(file: File) {
    val paletteState = rememberFilePaletteState()

    LaunchedEffect(file) {
        paletteState.generate(file)
    }

    // Use the palette...
}
```

### Direct Loader Usage

You can also use the loaders directly with the generic state functions:

```kotlin
// Using PlatformFileLoader
val paletteState = rememberPaletteState(loader = PlatformFileLoader)
LaunchedEffect(platformFile) {
    paletteState.generate(platformFile)
}

// Using FileLoader (Android only)
val paletteState = rememberPaletteState(loader = FileLoader)
LaunchedEffect(file) {
    paletteState.generate(file)
}
```

## API Reference

### Composables (All Platforms)

| Function                                   | Description                                  |
|--------------------------------------------|----------------------------------------------|
| `rememberPlatformFileDominantColorState()` | Returns a `DominantColorState<PlatformFile>` |
| `rememberPlatformFilePaletteState()`       | Returns a `PaletteState<PlatformFile>`       |

### Composables (Android Only)

| Function                           | Description                          |
|------------------------------------|--------------------------------------|
| `rememberFileDominantColorState()` | Returns a `DominantColorState<File>` |
| `rememberFilePaletteState()`       | Returns a `PaletteState<File>`       |

## Migration from v3.x

If you were using Okio's `Path` or `FilePathLoader` from version 3.x, see the
[Migration Guide](../MIGRATION.md) for instructions on updating your code.

**Key changes:**

- `PathLoader` and `FilePathLoader` have been removed
- Use `PlatformFileLoader` with FileKit's `PlatformFile` instead
- Okio is no longer a dependency
