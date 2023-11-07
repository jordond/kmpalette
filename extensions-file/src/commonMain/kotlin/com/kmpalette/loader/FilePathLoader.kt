package com.kmpalette.loader

import androidx.compose.ui.graphics.ImageBitmap
import okio.Path.Companion.toPath

/**
 * A [ImageBitmapLoader] that loads [ImageBitmap]s from a [String].
 *
 * **Warning:** The [String] must be a valid absolute file path that you have permission to read.
 *
 * You must import the Okio library to use this loader.
 */
public object FilePathLoader : ImageBitmapLoader<String> {

    override suspend fun load(input: String): ImageBitmap {
        return PathLoader.load(input.toPath())
    }
}