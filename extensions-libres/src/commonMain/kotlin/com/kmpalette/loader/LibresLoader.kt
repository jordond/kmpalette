package com.kmpalette.loader

import androidx.compose.ui.graphics.ImageBitmap
import io.github.skeptick.libres.images.Image

/**
 * A [ImageBitmapLoader] that loads a [ImageBitmap] from a Libres [Image].
 *
 * **Warning:** Only supports _non_ vector images, like PNGs and JPEGs.
 */
public data object LibresLoader : ImageBitmapLoader<Image> {

    /**
     * Loads a [ImageBitmap] from a Libres [Image].
     *
     * **Warning:** Only supports _non_ vector images, like PNGs and JPEGs.
     */
    override suspend fun load(input: Image): ImageBitmap {
        return input.toImageBitmap()
    }
}