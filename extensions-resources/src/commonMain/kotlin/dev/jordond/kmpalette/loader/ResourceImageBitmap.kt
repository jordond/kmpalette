package dev.jordond.kmpalette.loader

import androidx.compose.ui.graphics.ImageBitmap
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Resource

/**
 * Load the [Resource] than convert it to a [ByteArray] and load it with [ByteArrayLoader].
 */
@ExperimentalResourceApi
internal suspend fun Resource.toImageBitmap(): ImageBitmap {
    return ByteArrayLoader.load(readBytes())
}