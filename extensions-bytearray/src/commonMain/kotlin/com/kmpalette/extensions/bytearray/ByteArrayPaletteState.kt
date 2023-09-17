package com.kmpalette.extensions.bytearray

import androidx.compose.runtime.Composable
import androidx.palette.graphics.Palette
import com.kmpalette.PaletteState
import com.kmpalette.PaletteState.Companion.DEFAULT_CACHE_SIZE
import com.kmpalette.loader.ByteArrayLoader
import com.kmpalette.rememberPaletteState
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * Wrapper around [rememberPaletteState] that uses [ByteArrayLoader] to load the image.
 *
 * @see rememberPaletteState
 * @param[cacheSize] The maximum number of [Palette]s to cache. If 0, no caching will be done.
 * @param[coroutineContext] The [CoroutineContext] to use for generating [Palette]s.
 * @param[builder] A lambda that will be applied to the [Palette.Builder] to customize the
 * generation of the [Palette].
 * @return A [PaletteState] that will be remembered across composition.
 */
@Composable
public fun rememberByteArrayPaletteState(
    cacheSize: Int = DEFAULT_CACHE_SIZE,
    coroutineContext: CoroutineContext = Dispatchers.Default,
    builder: Palette.Builder.() -> Unit = {},
): PaletteState<ByteArray> = rememberPaletteState(
    loader = ByteArrayLoader,
    cacheSize = cacheSize,
    coroutineContext = coroutineContext,
    builder = builder,
)
