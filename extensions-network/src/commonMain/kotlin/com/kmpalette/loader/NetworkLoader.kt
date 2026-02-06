package com.kmpalette.loader

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.Url

/**
 * A [ImageBitmapLoader] that loads [ImageBitmap]s from a [Url].
 *
 * You must import the Ktor client library to use this loader.
 *
 * @param[httpClient] The [HttpClient] to use for loading the [ImageBitmap].
 * @param[requestBuilder] The [HttpRequestBuilder] to use for loading the [ImageBitmap].
 */
public class NetworkLoader(
    private val httpClient: HttpClient = HttpClient(),
    private val requestBuilder: HttpRequestBuilder = HttpRequestBuilder(),
) : ImageBitmapLoader<Url> {
    /**
     * Loads an [ImageBitmap] from a [Url].
     */
    override suspend fun load(input: Url): ImageBitmap = input.toImageBitmap(httpClient, requestBuilder)

    public companion object {
        private val httpClient: HttpClient = HttpClient()
        private val requestBuilder: HttpRequestBuilder = HttpRequestBuilder()

        /**
         * The default [NetworkLoader] instance.
         */
        public val Default: NetworkLoader = NetworkLoader(httpClient, requestBuilder)
    }
}

/**
 * Create and remember a [ImageBitmapLoader] that loads [ImageBitmap]s from a [Url].
 *
 * You must import the Ktor client library to use this loader.
 *
 * @param[httpClient] The [HttpClient] to use for loading the [ImageBitmap].
 * @param[requestBuilder] The [HttpRequestBuilder] to use for loading the [ImageBitmap].
 */
@Composable
public fun rememberNetworkLoader(
    httpClient: HttpClient = HttpClient(),
    requestBuilder: HttpRequestBuilder = HttpRequestBuilder(),
): NetworkLoader =
    remember(httpClient, requestBuilder) {
        NetworkLoader(httpClient, requestBuilder)
    }
