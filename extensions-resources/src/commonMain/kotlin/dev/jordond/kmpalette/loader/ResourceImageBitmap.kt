package dev.jordond.kmpalette.loader

import androidx.compose.ui.graphics.ImageBitmap
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Resource

@ExperimentalResourceApi
internal suspend fun Resource.toImageBitmap(): ImageBitmap {
    return ByteArrayLoader.load(readBytes())
}