package dev.jordond.kmpalette.loader

import androidx.compose.ui.graphics.ImageBitmap
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Resource

@ExperimentalResourceApi
public suspend fun Resource.toImageBitmap(): ImageBitmap {
    return readBytes().toImageBitmap()
}

@ExperimentalResourceApi
public suspend fun ImageBitmap.Companion.fromResource(resource: Resource): ImageBitmap {
    return resource.toImageBitmap()
}