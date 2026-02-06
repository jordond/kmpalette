package com.kmpalette.loader

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.decodeToImageBitmap
import java.io.File

/**
 * An [ImageBitmapLoader] that loads [ImageBitmap] from a [java.io.File].
 *
 * This loader is only available on Android. For cross-platform file loading,
 * use [PlatformFileLoader] instead.
 */
public object FileLoader : ImageBitmapLoader<File> {
    override suspend fun load(input: File): ImageBitmap = input.readBytes().decodeToImageBitmap()
}
