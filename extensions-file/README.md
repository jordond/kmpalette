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
        commonMain.dependencies {
            implementation(libs.kmpalette.core)
            implementation(libs.kmpalette.extensions.file)
            implementation("io.github.vinceglb:filekit-core:<version>")
        }
    }
}
```

FileKit is an `implementation` dependency of this module, so it is **not** exposed transitively.
Declare `filekit-core` yourself to construct a `PlatformFile`.

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

### PlatformFile (All Platforms)

Use `rememberPlatformFileDominantColorState` or `rememberPlatformFilePaletteState` to work with
FileKit's `PlatformFile`. The dominant-color function requires both `defaultColor` and
`defaultOnColor`. They are used until generation succeeds, and whenever it fails. Nothing here
assumes Material 3, so pass whatever your theme uses.

```kotlin
@Composable
fun MyComposable(file: PlatformFile) {
    val dominantColorState = rememberPlatformFileDominantColorState(
        defaultColor = MaterialTheme.colorScheme.primary,
        defaultOnColor = MaterialTheme.colorScheme.onPrimary,
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

Reading a file can fail, so surface errors through `paletteState.state`:

```kotlin
when (val state = paletteState.state) {
    null -> Unit // nothing generated yet
    PaletteResult.Loading -> CircularProgressIndicator()
    is PaletteResult.Error -> Text("Failed to read file: ${state.cause.message}")
    is PaletteResult.Success -> PaletteDisplay(state.palette)
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

    val vibrant = paletteState.palette?.vibrantSwatch
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

| Function                                                                    | Description                                  |
|-----------------------------------------------------------------------------|----------------------------------------------|
| `rememberPlatformFileDominantColorState(defaultColor, defaultOnColor, ...)` | Returns a `DominantColorState<PlatformFile>` |
| `rememberPlatformFilePaletteState(...)`                                     | Returns a `PaletteState<PlatformFile>`       |

### Composables (Android Only)

| Function                                                            | Description                          |
|---------------------------------------------------------------------|--------------------------------------|
| `rememberFileDominantColorState(defaultColor, defaultOnColor, ...)` | Returns a `DominantColorState<File>` |
| `rememberFilePaletteState(...)`                                     | Returns a `PaletteState<File>`       |

## Migration from v3.x

If you were using Okio's `Path` or `FilePathLoader` from version 3.x, see the
[Migration Guide](../MIGRATION.md) for instructions on updating your code.

**Key changes:**

- `PathLoader` and `FilePathLoader` have been removed
- Use `PlatformFileLoader` with FileKit's `PlatformFile` instead
- Okio is no longer a dependency
