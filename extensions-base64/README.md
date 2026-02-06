# Base64 Extensions

This extension provides `Base64Loader` an `ImageBitmapLoader` for loading base64 encoded images to
an `ImageBitmap`.

| Loader         | Input Class |
|----------------|-------------|
| `Base64Loader` | `String`    |

## Setup

You need to add the following to your `build.gradle.kts` file:

```kotlin
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kmpalette.core)
                implementation(libs.kmpalette.extensions.base64)
            }
        }
    }
}
```

## Usage

You will need to have an image (png, jpg, etc) encoded as a Base64 `String`. You can then use the
dedicated composable functions to generate a palette or dominant color.

**Note:** If there is a header on the Base64 `String` (e.g., `data:image/png;base64,`) it will be
stripped and ignored.

### Dominant Color

Use `rememberBase64DominantColorState` to extract the dominant color from a Base64 encoded image:

```kotlin
@Composable
fun MyComposable(base64Image: String) {
    val dominantColorState = rememberBase64DominantColorState()
    LaunchedEffect(base64Image) {
        dominantColorState.updateFrom(base64Image)
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

### Palette Generation

Use `rememberBase64PaletteState` to generate a full color palette from a Base64 encoded image:

```kotlin
@Composable
fun MyComposable(base64Image: String) {
    val paletteState = rememberBase64PaletteState()

    LaunchedEffect(base64Image) {
        paletteState.generate(base64Image)
    }

    // Access different swatches
    val vibrant = paletteState.palette?.vibrantSwatch
    val muted = paletteState.palette?.mutedSwatch

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(vibrant?.color ?: Color.White)
    ) {
        Text("Hello", color = vibrant?.onColor ?: Color.Black)
    }
}
```

### Direct Loader Usage

You can also use `Base64Loader` directly with the generic state functions:

```kotlin
val base64Image = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAAD" +
        "gdz34AAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAApgAAAKYB3X3/OAAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3" +
        "NjYXBlLm9yZ5vuPBoAAANCSURBVEiJtZZPbBtFFMZ/M7ubXdtdb1xSFyeilBapySVU8h8OoFaooFSqiihIVIpQB" +
        "Kci6KEg9Q6H9kovIHoCIVQJJCKE1ENFjnAgcaSGC6rEnxBwA04Tx43t2FnvDAfjkNibxgHxnWb2e/u992bee7tC" +
        "..."

// Direct bitmap loading
val imageBitmap: ImageBitmap = Base64Loader.load(base64Image)

// Or using the generic composable with loader
val paletteState = rememberPaletteState(loader = Base64Loader)
LaunchedEffect(base64Image) {
    paletteState.generate(base64Image)
}
```

## API Reference

### Composables

| Function                             | Description                                                           |
|--------------------------------------|-----------------------------------------------------------------------|
| `rememberBase64DominantColorState()` | Returns a `DominantColorState<String>` for extracting dominant colors |
| `rememberBase64PaletteState()`       | Returns a `PaletteState<String>` for generating full palettes         |
