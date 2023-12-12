package com.kmpalette.loader

import androidx.compose.ui.graphics.ImageBitmap

/**
 * A default loader that takes an [ImageBitmap] and returns it.
 */
internal val ImageBitmapLoader = object : ImageBitmapLoader<ImageBitmap> {

    override suspend fun load(input: ImageBitmap): ImageBitmap = input
}

