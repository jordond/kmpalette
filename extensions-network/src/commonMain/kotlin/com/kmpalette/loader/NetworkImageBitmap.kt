package com.kmpalette.loader

import androidx.compose.ui.graphics.ImageBitmap
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.statement.readBytes
import io.ktor.http.Url

internal suspend fun Url.toImageBitmap(
    client: HttpClient = HttpClient(),
    requestBuilder: HttpRequestBuilder = HttpRequestBuilder(),
): ImageBitmap {
    require(protocol.name == "https" || protocol.name == "http") {
        "Only http and https protocols are supported"
    }

    val response = client.request {
        takeFrom(requestBuilder)
        url(this@toImageBitmap)
    }

    return ByteArrayLoader.load(response.readBytes())
}