package com.kmpalette.loader

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
    override suspend fun load(input: Url): ImageBitmap {
        return input.toImageBitmap(httpClient, requestBuilder)
    }
}