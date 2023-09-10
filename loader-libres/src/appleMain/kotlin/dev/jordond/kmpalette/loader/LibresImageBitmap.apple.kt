package dev.jordond.kmpalette.loader

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import dev.jordond.kmpalette.loader.internal.toSkiaImage
import io.github.skeptick.libres.images.Image

public actual fun Image.toImageBitmap(): ImageBitmap {
    return toSkiaImage().toComposeImageBitmap()
}