package com.kmpalette.extensions.resource

import androidx.compose.runtime.Composable
import com.kmpalette.PaletteState
import com.kmpalette.loader.rememberResourceLoader
import com.kmpalette.palette.graphics.Palette
import com.kmpalette.rememberPaletteState
import kotlinx.coroutines.Dispatchers
import org.jetbrains.compose.resources.DrawableResource
import kotlin.coroutines.CoroutineContext

/**
 * Wrapper around [rememberPaletteState] that uses [com.kmpalette.loader.ResourceLoader] to load the image.
 *
 * @see rememberPaletteState
 * @param[cacheSize] The maximum number of [Palette]s to cache. If 0, no caching will be done.
 * @param[coroutineContext] The [CoroutineContext] to use for generating [Palette]s.
 * @param[builder] A lambda that will be applied to the [Palette.Builder] to customize the
 * generation of the [Palette].
 * @return A [PaletteState] that will be remembered across composition.
 */
@Composable
public fun rememberResourcePaletteState(
    cacheSize: Int = PaletteState.DEFAULT_CACHE_SIZE,
    coroutineContext: CoroutineContext = Dispatchers.Default,
    builder: Palette.Builder.() -> Unit = {},
): PaletteState<DrawableResource> = rememberPaletteState(
    loader = rememberResourceLoader(),
    cacheSize = cacheSize,
    coroutineContext = coroutineContext,
    builder = builder,
)