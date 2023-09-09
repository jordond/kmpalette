package dev.jordond.kmpalette.loader.libres

import androidx.compose.ui.graphics.ImageBitmap
import dev.jordond.kmpalette.loader.ImageBitmapLoader
import io.github.skeptick.libres.images.Image

public class LibresImageBitmapLoader : ImageBitmapLoader<Image> {

    override fun load(input: Image): ImageBitmap {
        return input.toImageBitmap()
    }
}

internal expect fun Image.toImageBitmap(): ImageBitmap