package dev.jordond.kmpalette.loader

import androidx.compose.ui.graphics.ImageBitmap

public interface ImageBitmapLoader<T> {

    public fun load(input: T): ImageBitmap
}
