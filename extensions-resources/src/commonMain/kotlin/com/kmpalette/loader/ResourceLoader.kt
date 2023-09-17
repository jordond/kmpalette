package com.kmpalette.loader

import androidx.compose.ui.graphics.ImageBitmap
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Resource

/**
 * A [ImageBitmapLoader] that loads [ImageBitmap]s from [Resource]s.
 *
 * **Warning:** Only supports _non_ vector images, like PNGs and JPEGs.
 *
 * Usage:
 *
 * ```
 * import org.jetbrains.compose.resources.resource
 *
 * val resImage = resource("my_image.png")
 * val imageBitmap = ResourceLoader.load(resImage)
 * ```
 */
@ExperimentalResourceApi
public data object ResourceLoader : ImageBitmapLoader<Resource> {

    /**
     * Load the [Resource] than convert it to a [ImageBitmap].
     *
     * Only supports _non_ vector images, like PNGs and JPEGs.
     *
     * @param[input] The [Resource] to load.
     */
    override suspend fun load(input: Resource): ImageBitmap {
        return input.toImageBitmap()
    }
}