package com.kmpalette.loader

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.decodeToImageBitmap
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.readBytes

public object PlatformFileLoader : ImageBitmapLoader<PlatformFile> {
    override suspend fun load(input: PlatformFile): ImageBitmap =
        input.readBytes().decodeToImageBitmap()
}