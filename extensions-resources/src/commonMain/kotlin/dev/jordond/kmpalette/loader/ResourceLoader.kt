package dev.jordond.kmpalette.loader

import androidx.compose.ui.graphics.ImageBitmap
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Resource

@ExperimentalResourceApi
public data object ResourceLoader : ImageBitmapLoader<Resource> {

    override suspend fun load(input: Resource): ImageBitmap {
        return ByteArrayLoader.load(input.readBytes())
    }
}