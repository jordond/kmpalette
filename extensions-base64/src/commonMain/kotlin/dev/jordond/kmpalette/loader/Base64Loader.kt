package dev.jordond.kmpalette.loader

import androidx.compose.ui.graphics.ImageBitmap
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@ExperimentalEncodingApi
public object Base64Loader : ImageBitmapLoader<String> {

    override suspend fun load(input: String): ImageBitmap {
        val base64Bytes = Base64.decode(input)
        return ByteArrayLoader.load(base64Bytes)
    }
}