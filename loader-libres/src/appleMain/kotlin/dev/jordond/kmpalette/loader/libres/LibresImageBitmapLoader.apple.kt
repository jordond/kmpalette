package dev.jordond.kmpalette.loader.libres

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import dev.jordond.kmpalette.loader.libres.internal.toSkiaImage
import io.github.skeptick.libres.images.Image

internal actual fun Image.toImageBitmap(): ImageBitmap {
    return toSkiaImage().toComposeImageBitmap()
}