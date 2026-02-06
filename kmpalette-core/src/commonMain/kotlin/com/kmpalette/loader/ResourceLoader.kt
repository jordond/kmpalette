package com.kmpalette.loader

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.decodeToImageBitmap
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ResourceEnvironment
import org.jetbrains.compose.resources.getDrawableResourceBytes
import org.jetbrains.compose.resources.rememberResourceEnvironment

/**
 * A [ImageBitmapLoader] that loads [ImageBitmap]s from a [DrawableResource], by reading the
 * bytes from the resource.
 *
 * @param[environment] The [ResourceEnvironment] for loading resources.
 */
public class ResourceLoader(
    private val environment: ResourceEnvironment,
) : ImageBitmapLoader<DrawableResource> {
    override suspend fun load(input: DrawableResource): ImageBitmap {
        val bytes = getDrawableResourceBytes(environment, input)
        return bytes.decodeToImageBitmap()
    }
}

/**
 * Create and remember a [ResourceLoader] for the current [ResourceEnvironment].
 *
 * @return A [ResourceLoader] for the current [ResourceEnvironment]
 */
@Composable
public fun rememberResourceLoader(): ResourceLoader {
    val environment = rememberResourceEnvironment()
    return remember(environment) {
        ResourceLoader(environment)
    }
}
