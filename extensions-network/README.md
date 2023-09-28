# Network Extensions

This extension provides `Network` a `ImageBitmapLoader` for loading images from a URL.

| Library                                | Loader          | Input Class | Demo                                                                                                             |
|----------------------------------------|-----------------|-------------|------------------------------------------------------------------------------------------------------------------|
| [ktor](https://github.com/ktorio/ktor) | `NetworkLoader` | `Url`       | [`NetworkDemoScreen`](../demo/composeApp/src/commonMain/kotlin/com/kmpalette/demo/dominant/NetworkDemoScreen.kt) |

## Setup

In order to use these extensions you will need to add the Ktor dependency to
your `build.gradle.kts`, you will need the `core` library as well as a client for each of your
supported platforms.

Then you need to add the following to your `build.gradle.kts` file:

```kotlin
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kmpalette.core)
                implementation("io.ktor:ktor-client-core:$ktor_version")
            }
        }

        androidMain {
            dependencies {
                implementation("io.ktor:ktor-client-android:$ktor_version")
            }
        }

        iosMain {
            dependencies {
                implementation("io.ktor:ktor-client-darwin:$ktor_version")
            }
        }

        jvmMain {
            dependencies {
                implementation("io.ktor:ktor-client-okhttp:$ktor_version")
            }
        }

        jsMain {
            dependencies {
                implementation("io.ktor:ktor-client-js:$ktor_version")
            }
        }
    }
}
```

## Usage

Now you can use the `NetworkLoader` to load images from a URL.

You can customize the behaviour of the request by passing in your own `HttpClient`
of `HttpRequestBuilder`:

```kotlin
class NetworkLoader(
    private val httpClient: HttpClient = HttpClient(),
    private val requestBuilder: HttpRequestBuilder = HttpRequestBuilder(),
) : ImageBitmapLoader<Url>
```

But if you are using the loader inside of a `Composable` make sure it is remembered, or use
the `rememberNetworkLoader` function.

Now you can pass a `Url` object to load an image:

```kotlin
@Composable
fun MyComposable(url: Url) {
    val networkLoader = rememberNetworkLoader()
    val paletteState = rememberPaletteState(loader = networkLoader)
    LaunchedEffect(url) {
        paletteState.generate(url)
    }
}
```

The Image will be fetched, converted into a `ImageBitmap` then a palette will be generated.