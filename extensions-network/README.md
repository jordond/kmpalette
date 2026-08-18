# Network Extensions

This extension provides `NetworkLoader` an `ImageBitmapLoader` for loading images from a URL using
[Ktor](https://github.com/ktorio/ktor).

| Library                                | Loader          | Input Class |
|----------------------------------------|-----------------|-------------|
| [Ktor](https://github.com/ktorio/ktor) | `NetworkLoader` | `Url`       |

## Setup

To use this extension you need to add the Ktor dependency to your `build.gradle.kts`.
You will need the `core` library as well as a client engine for each of your supported platforms.

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kmpalette.core)
            implementation(libs.kmpalette.extensions.network)
            implementation("io.ktor:ktor-client-core:$ktor_version")
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
}
```

Only the `http` and `https` protocols are supported; any other `Url` protocol throws an
`IllegalArgumentException`.

## Usage

### Dominant Color

Use `rememberNetworkDominantColorState` to extract the dominant color from a remote image. Both
`defaultColor` and `defaultOnColor` are required. They are used until generation succeeds, and
whenever it fails. Nothing here assumes Material 3, so pass whatever your theme uses.

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

The state of the last generation is available on `dominantColorState.result` as a `PaletteResult`,
which is how you surface network failures.

### Palette Generation

Use `rememberNetworkPaletteState` to generate a full color palette from a remote image:

```kotlin
@Composable
fun MyComposable(imageUrl: Url) {
    val paletteState = rememberNetworkPaletteState()

    LaunchedEffect(imageUrl) {
        paletteState.generate(imageUrl)
    }

    // Access different swatches through the generated palette
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

To render loading and error states, use `paletteState.state`:

```kotlin
when (val state = paletteState.state) {
    null -> Unit // nothing generated yet
    PaletteResult.Loading -> CircularProgressIndicator()
    is PaletteResult.Error -> Text("Failed to load image: ${state.cause.message}")
    is PaletteResult.Success -> PaletteDisplay(state.palette)
}
```

### One-shot Generation

If you don't need a reusable state object, `Url.rememberGeneratePalette` loads the image and
generates a palette in one step:

```kotlin
@Composable
fun MyComposable(imageUrl: Url) {
    val result = imageUrl.rememberGeneratePalette()
    val palette = result.paletteOrNull
}
```

**Note:** this overload uses a default `HttpClient`. Use `rememberNetworkPaletteState` if you need
to supply your own.

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

    val dominantColorState = rememberNetworkDominantColorState(
        defaultColor = MaterialTheme.colorScheme.primary,
        defaultOnColor = MaterialTheme.colorScheme.onPrimary,
        httpClient = httpClient,
    )
    LaunchedEffect(imageUrl) {
        dominantColorState.updateFrom(imageUrl)
    }
}
```

`rememberNetworkPaletteState` accepts the same `httpClient` and `httpRequestBuilder` parameters.

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

| Function                                                               | Description                                                        |
|------------------------------------------------------------------------|--------------------------------------------------------------------|
| `rememberNetworkDominantColorState(defaultColor, defaultOnColor, ...)` | Returns a `DominantColorState<Url>` for extracting dominant colors  |
| `rememberNetworkPaletteState(...)`                                     | Returns a `PaletteState<Url>` for generating full palettes          |
| `rememberNetworkLoader(httpClient, requestBuilder)`                    | Returns a remembered `NetworkLoader` instance                       |
| `Url.rememberGeneratePalette(...)`                                     | One-shot load + generate, returning a `PaletteResult`               |
