package dev.jordond.kmpalette.loader

import androidx.compose.ui.graphics.ImageBitmap
import io.github.skeptick.libres.images.Image

public data object LibresLoader : ImageBitmapLoader<Image> {

    override suspend fun load(input: Image): ImageBitmap {
        return input.toImageBitmap()
    }
}