package dev.jordond.kmpalette.loader

import androidx.compose.ui.graphics.ImageBitmap
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Resource

@ExperimentalResourceApi
public object ResourcesLoader : ImageBitmapLoader<Resource> {

    override suspend fun load(input: Resource): ImageBitmap {
        return input.readBytes().toImageBitmap()
    }
}