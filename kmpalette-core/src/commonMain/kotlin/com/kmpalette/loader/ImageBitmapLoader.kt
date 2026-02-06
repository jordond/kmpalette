package com.kmpalette.loader

import androidx.compose.ui.graphics.ImageBitmap

public interface ImageBitmapLoader<T> {
    public suspend fun load(input: T): ImageBitmap
}
