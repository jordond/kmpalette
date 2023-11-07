# File Extensions

This extension provides `FilePathLoader` and `PathLoader` a `ImageBitmapLoader` for loading images
from an absolute path, or from an Okio `Path` object.

| Library                          | Loader                          | Input Class       | Demo |
|----------------------------------|---------------------------------|-------------------|------|
| [okio](https://square.github.io) | `PathLoader` / `FilePathLoader` | `Path` / `String` | N/A  |

## Setup

In order to use these extensions you will need to add the Okio dependency to
your `build.gradle.kts`, you will need the `core` library as well.

Then you need to add the following to your `build.gradle.kts` file:

```kotlin
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kmpalette.core)
                implementation(libs.kmpalette.extensions.file)
                implementation("com.squareup.okio:okio:<latest okio version>")
            }
        }
    }
}
```

## Usage

Now you can use the `FilePathLoader` to load images from an absolute path:

```kotlin
@Composable
fun MyComposable(absolutePath: String) {
    val paletteState = rememberPaletteState(loader = FilePathLoader)
    LaunchedEffect(absolutePath) {
        paletteState.generate(absolutePath)
    }
}
```

Or if you already have a `Path` object:

```kotlin
@Composable
fun MyComposable(path: Path) {
    val paletteState = rememberPaletteState(loader = PathLoader)
    LaunchedEffect(path) {
        paletteState.generate(path)
    }
}
```

The Image will be fetched, converted into a `ImageBitmap` then a palette will be generated.