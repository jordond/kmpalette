package dev.jordond.kmpalette.loader

import androidx.compose.ui.graphics.ImageBitmap

public data object ByteArrayLoader : ImageBitmapLoader<ByteArray> {

    override suspend fun load(input: ByteArray): ImageBitmap {
        return input.toImageBitmap()
    }
}