package dev.jordond.kmpalette

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import dev.jordond.kmpalette.loader.NetworkLoader
import dev.jordond.kmpalette.palette.graphics.Palette
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.Url
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

@Composable
public fun rememberNetworkDominantColorState(
    defaultColor: Color,
    defaultOnColor: Color,
    cacheSize: Int = DominantColorState.DEFAULT_CACHE_SIZE,
    httpClient: HttpClient = HttpClient(),
    httpRequestBuilder: HttpRequestBuilder = HttpRequestBuilder(),
    coroutineContext: CoroutineContext = Dispatchers.Default,
    isColorValid: (Color) -> Boolean = { true },
    builder: Palette.Builder.() -> Unit = {},
): DominantColorState<Url> = rememberDominantColorState(
    loader = NetworkLoader(httpClient, httpRequestBuilder),
    defaultColor = defaultColor,
    defaultOnColor = defaultOnColor,
    cacheSize = cacheSize,
    coroutineContext = coroutineContext,
    isColorValid = isColorValid,
    builder = builder,
)
