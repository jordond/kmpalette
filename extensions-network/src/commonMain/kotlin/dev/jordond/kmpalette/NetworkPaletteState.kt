package dev.jordond.kmpalette

import androidx.compose.runtime.Composable
import dev.jordond.kmpalette.DominantColorState.Companion.DEFAULT_CACHE_SIZE
import dev.jordond.kmpalette.loader.NetworkLoader
import dev.jordond.kmpalette.palette.graphics.Palette
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.Url
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

@Composable
public fun rememberNetworkPaletteState(
    cacheSize: Int = DEFAULT_CACHE_SIZE,
    httpClient: HttpClient = HttpClient(),
    httpRequestBuilder: HttpRequestBuilder = HttpRequestBuilder(),
    coroutineContext: CoroutineContext = Dispatchers.Default,
    builder: Palette.Builder.() -> Unit = {},
): PaletteState<Url> = rememberPaletteState(
    loader = NetworkLoader(httpClient, httpRequestBuilder),
    cacheSize = cacheSize,
    coroutineContext = coroutineContext,
    builder = builder,
)