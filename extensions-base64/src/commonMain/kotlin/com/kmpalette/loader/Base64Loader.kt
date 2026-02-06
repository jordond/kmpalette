package com.kmpalette.loader

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.decodeToImageBitmap
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * An [ImageBitmapLoader] that decodes Base64-encoded image strings into [ImageBitmap].
 *
 * The input string can optionally include a data URI prefix (e.g., `data:image/png;base64,`)
 * which will be automatically stripped before decoding.
 *
 * @throws IllegalArgumentException when the Base64 string is padded incorrectly or contains
 * extra symbols after the padding.
 */
public object Base64Loader : ImageBitmapLoader<String> {
    /**
     * @throws[IllegalArgumentException] - when the symbols for decoding are padded incorrectly or
     * there are extra symbols after the padding.
     */
    @OptIn(ExperimentalEncodingApi::class)
    override suspend fun load(input: String): ImageBitmap {
        val base64Bytes = Base64.decode(input.stripBase64Prefix())
        return base64Bytes.decodeToImageBitmap()
    }
}

internal fun String.stripBase64Prefix(): String = if (contains(",")) substringAfter(",") else this
