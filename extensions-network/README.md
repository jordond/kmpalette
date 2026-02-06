# Network Extensions

This extension provides `NetworkLoader` an `ImageBitmapLoader` for loading images from a URL using
[Ktor](https://github.com/ktorio/ktor).

| Library                                | Loader          | Input Class |
|----------------------------------------|-----------------|-------------|
| [Ktor](https://github.com/ktorio/ktor) | `NetworkLoader` | `Url`       |

## Setup

In order to use this extension you will need to add the Ktor dependency to your `build.gradle.kts`.
You will need the `core` library as well as a client engine for each of your supported platforms.

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kmpalette.core)
            implementation("io.ktor:ktor-client-core:$ktor_version")
        }
    }

    androidMain.dependencies {
        implementation("io.ktor:ktor-client-android:$ktor_version")
    }

    iosMain.dependencies {
        implementation("io.ktor:ktor-client-darwin:$ktor_version")
    }

    jvmMain.dependencies {
        implementation("io.ktor:ktor-client-okhttp:$ktor_version")
    }

    webMain.dependencies {
        implementation("io.ktor:ktor-client-js:$ktor_version")
    }
}
```

## Usage

### Dominant Color

Use `rememberNetworkDominantColorState` to extract the dominant color from a remote image:

```kotlin
@Composable
fun MyComposable(imageUrl: Url) {
    val dominantColorState = rememberNetworkDominantColorState(
        defaultColor = MaterialTheme.colorScheme.primary,
        defaultOnColor = MaterialTheme.colorScheme.onPrimary,
    )

    LaunchedEffect(imageUrl) {
        dominantColorState.updateFrom(imageUrl)
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

Use `rememberNetworkPaletteState` to generate a full color palette from a remote image:

```kotlin
@Composable
fun MyComposable(imageUrl: Url) {
    val paletteState = rememberNetworkPaletteState()

    LaunchedEffect(imageUrl) {
        paletteState.generate(imageUrl)
    }

    // Access different swatches
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

### Custom HttpClient

You can customize the behavior of the network requests by passing your own `HttpClient`
or `HttpRequestBuilder`:

```kotlin
@Composable
fun MyComposable(imageUrl: Url) {
    // Create a custom HttpClient with logging, timeouts, etc.
    val httpClient = remember {
        HttpClient {
            install(Logging) {
                level = LogLevel.INFO
            }
        }
    }

    val dominantColorState = rememberNetworkDominantColorState(httpClient = httpClient)
    LaunchedEffect(imageUrl) {
        dominantColorState.updateFrom(imageUrl)
    }
}
```

### Direct Loader Usage

You can also use `NetworkLoader` directly with the generic state functions:

```kotlin
@Composable
fun MyComposable(imageUrl: Url) {
    val networkLoader = rememberNetworkLoader()
    val paletteState = rememberPaletteState(loader = networkLoader)

    LaunchedEffect(imageUrl) {
        paletteState.generate(imageUrl)
    }
}
```

Or use the `NetworkLoader.Default` instance for non-composable contexts:

```kotlin
suspend fun loadPalette(url: Url): Palette {
    val bitmap = NetworkLoader.Default.load(url)
    return Palette.from(bitmap).generate()
}
```

## API Reference

### Composables

| Function                              | Description                                                        |
|---------------------------------------|--------------------------------------------------------------------|
| `rememberNetworkDominantColorState()` | Returns a `DominantColorState<Url>` for extracting dominant colors |
| `rememberNetworkPaletteState()`       | Returns a `PaletteState<Url>` for generating full palettes         |
| `rememberNetworkLoader()`             | Returns a remembered `NetworkLoader` instance                      |
