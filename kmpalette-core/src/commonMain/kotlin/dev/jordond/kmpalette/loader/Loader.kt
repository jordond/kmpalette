package dev.jordond.kmpalette.loader

import androidx.compose.ui.graphics.ImageBitmap

internal val ImageBitmapLoader = object : ImageBitmapLoader<ImageBitmap> {
    override suspend fun load(input: ImageBitmap): ImageBitmap = input
}