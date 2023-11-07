package com.kmpalette.loader

import androidx.compose.ui.graphics.ImageBitmap
import okio.Path

/**
 * A [ImageBitmapLoader] that loads [ImageBitmap]s from a [Path].
 *
 * You must import the Okio library to use this loader.
 */
public object PathLoader : ImageBitmapLoader<Path> {

    override suspend fun load(input: Path): ImageBitmap {
        return input.toImageBitmap()
    }
}