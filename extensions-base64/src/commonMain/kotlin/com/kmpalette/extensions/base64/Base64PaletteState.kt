package com.kmpalette.extensions.base64

import androidx.compose.runtime.Composable
import androidx.palette.graphics.Palette
import com.kmpalette.PaletteState
import com.kmpalette.loader.Base64Loader
import com.kmpalette.rememberPaletteState
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * Wrapper around [rememberPaletteState] that uses [Base64Loader] to load the image.
 *
 * @see rememberPaletteState
 * @param[cacheSize] The maximum number of [Palette]s to cache. If 0, no caching will be done.
 * @param[coroutineContext] The [CoroutineContext] to use for generating [Palette]s.
 * @param[builder] A lambda that will be applied to the [Palette.Builder] to customize the
 * generation of the [Palette].
 * @return A [PaletteState] that will be remembered across composition.
 */
@Composable
public fun rememberBase64PaletteState(
    cacheSize: Int = PaletteState.DEFAULT_CACHE_SIZE,
    coroutineContext: CoroutineContext = Dispatchers.Default,
    builder: Palette.Builder.() -> Unit = {},
): PaletteState<String> = rememberPaletteState(
    loader = Base64Loader,
    cacheSize = cacheSize,
    coroutineContext = coroutineContext,
    builder = builder,
)
