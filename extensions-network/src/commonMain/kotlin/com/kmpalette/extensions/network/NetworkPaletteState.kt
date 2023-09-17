package com.kmpalette.extensions.network

import androidx.compose.runtime.Composable
import androidx.palette.graphics.Palette
import com.kmpalette.PaletteState
import com.kmpalette.loader.NetworkLoader
import com.kmpalette.rememberPaletteState
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.Url
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * Wrapper around [rememberPaletteState] that uses [NetworkLoader] to load the image.
 *
 * You must import the Ktor client library to use this loader.
 *
 * @see rememberPaletteState
 * @param[cacheSize] The maximum number of [Palette]s to cache. If 0, no caching will be done.
 * @param[coroutineContext] The [CoroutineContext] to use for generating [Palette]s.
 * @param[builder] A lambda that will be applied to the [Palette.Builder] to customize the
 * generation of the [Palette].
 * @return A [PaletteState] that will be remembered across composition.
 */
@Composable
public fun rememberNetworkPaletteState(
    cacheSize: Int = PaletteState.DEFAULT_CACHE_SIZE,
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