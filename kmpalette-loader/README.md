# kmpalette-loader

This module contains a single interface: `ImageBitmapLoader<T>`, the contract every kmpalette image
source implements.

```kotlin
public interface ImageBitmapLoader<T> {
    public suspend fun load(input: T): ImageBitmap
}
```

It exists as its own artifact so that a module can implement or consume the loader contract without
depending on all of [`core`](../README.md).

## Setup

You usually **do not** need to declare this dependency. Both `core` and every `extensions-*`
artifact expose it with `api`, so it is already on your compile classpath.

Add it explicitly only when you're writing a loader in a module that doesn't otherwise depend on
`core`:

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("com.materialkolor.palette:kmpalette-loader:<version>")
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

## Writing a loader

Implement `ImageBitmapLoader<T>` for whatever type you load images from:

```kotlin
object MyLoader : ImageBitmapLoader<MyImageRef> {
    override suspend fun load(input: MyImageRef): ImageBitmap {
        val bytes: ByteArray = fetchBytes(input)
        return bytes.decodeToImageBitmap()
    }
}
```

Then pass it to the generic state functions from `core`:

```kotlin
val paletteState = rememberPaletteState(loader = MyLoader)
LaunchedEffect(ref) {
    paletteState.generate(ref)
}

val dominantColorState = rememberDominantColorState(loader = MyLoader)
LaunchedEffect(ref) {
    dominantColorState.updateFrom(ref)
}
```

Throwing from `load` is fine. The state objects catch it and surface it as
`PaletteResult.Error`. `CancellationException` is rethrown, so coroutine cancellation works as
expected.

## Built-in loaders

| Loader               | Input              | Module               |
|----------------------|--------------------|----------------------|
| `ByteArrayLoader`    | `ByteArray`        | `core`               |
| `PainterLoader`      | `Painter`          | `core`               |
| `ResourceLoader`     | `DrawableResource` | `core`               |
| `DrawableLoader`     | `@DrawableRes Int` | `core` (Android)     |
| `Base64Loader`       | `String`           | `extensions-base64`  |
| `NetworkLoader`      | `Url`              | `extensions-network` |
| `PlatformFileLoader` | `PlatformFile`     | `extensions-file`    |
| `FileLoader`         | `java.io.File`     | `extensions-file` (Android) |
