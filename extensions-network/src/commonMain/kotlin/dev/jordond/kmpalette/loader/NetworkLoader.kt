package dev.jordond.kmpalette.loader

import androidx.compose.ui.graphics.ImageBitmap
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.Url

public class NetworkLoader(
    private val httpClient: HttpClient = HttpClient(),
    private val requestBuilder: HttpRequestBuilder = HttpRequestBuilder(),
) : ImageBitmapLoader<Url> {

    override suspend fun load(input: Url): ImageBitmap {
        return input.toImageBitmap(httpClient, requestBuilder)
    }
}