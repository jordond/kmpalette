package com.kmpalette.loader

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.kmpalette.loader.internal.toSkiaImage
import io.github.skeptick.libres.images.Image

internal actual suspend fun Image.toImageBitmap(): ImageBitmap {
    return toSkiaImage().toComposeImageBitmap()
}