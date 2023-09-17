package com.kmpalette.loader

import androidx.compose.ui.graphics.ImageBitmap

/**
 * A [ImageBitmapLoader] that loads [ImageBitmap]s from [ByteArray]s.
 **/
public data object ByteArrayLoader : ImageBitmapLoader<ByteArray> {

    /**
     * Loads an [ImageBitmap] from a [ByteArray].
     */
    override suspend fun load(input: ByteArray): ImageBitmap {
        return input.toImageBitmap()
    }
}