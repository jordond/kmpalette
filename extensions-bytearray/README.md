# ByteArray Extensions

This extension provides `ByteArrayLoader` a `ImageBitmapLoader` for loading image bytes to
a `ImageBitmap`.

| Loader            | Input Class | Demo |
|-------------------|-------------|------|
| `ByteArrayLoader` | `ByteArray` | N/A  |

# Setup

You need to add the following to your `build.gradle.kts` file:

```kotlin
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kmpalette.core)
                implementation(libs.kmpalette.extensions.bytearray)
            }
        }
    }
}
```

## Usage

You will need to have a `ByteArray` containing all the bytes of an image. Typically this extension
isn't used directly, but instead used by other extensions that provide a `ByteArray` as an output:

- `extensions-resources`
- `extensions-network`
