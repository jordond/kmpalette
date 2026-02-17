package com.kmpalette.loader

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.decodeToImageBitmap

/**
 * A default loader that converts a [ByteArray] to an [ImageBitmap] for palette generation.
 */
public object ByteArrayLoader : ImageBitmapLoader<ByteArray> {
    override suspend fun load(input: ByteArray): ImageBitmap = input.decodeToImageBitmap()
}

