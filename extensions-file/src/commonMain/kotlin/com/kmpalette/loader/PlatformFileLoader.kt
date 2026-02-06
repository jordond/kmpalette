package com.kmpalette.loader

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.decodeToImageBitmap
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.readBytes

/**
 * An [ImageBitmapLoader] that loads [ImageBitmap] from a [PlatformFile].
 *
 * This loader uses FileKit's [PlatformFile] which works across all supported platforms
 * (Android, iOS, Desktop, Web).
 */
public object PlatformFileLoader : ImageBitmapLoader<PlatformFile> {
    override suspend fun load(input: PlatformFile): ImageBitmap = input.readBytes().decodeToImageBitmap()
}
