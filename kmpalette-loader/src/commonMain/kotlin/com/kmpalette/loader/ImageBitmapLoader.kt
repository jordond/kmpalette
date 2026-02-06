package com.kmpalette.loader

import androidx.compose.ui.graphics.ImageBitmap

/**
 * A loader that can load an [ImageBitmap] from a given input <T>.
 */
public interface ImageBitmapLoader<T> {

    /**
     * Loads an [ImageBitmap] from the given input [T].
     *
     * @param[input] The input to load the [ImageBitmap] from.
     * @return The loaded [ImageBitmap].
     */
    public suspend fun load(input: T): ImageBitmap
}
