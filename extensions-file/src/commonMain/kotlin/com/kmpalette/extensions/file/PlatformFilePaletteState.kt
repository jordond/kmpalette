package com.kmpalette.extensions.file

import androidx.compose.runtime.Composable
import com.kmpalette.PaletteState
import com.kmpalette.loader.PlatformFileLoader
import com.kmpalette.palette.graphics.Palette
import com.kmpalette.rememberPaletteState
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * Wrapper around [rememberPaletteState] that uses [PlatformFileLoader] to load the image.
 *
 * @see rememberPaletteState
 * @param[cacheSize] The maximum number of [Palette]s to cache. If 0, no caching will be done.
 * @param[coroutineContext] The [CoroutineContext] to use for generating [Palette]s.
 * @param[builder] A lambda that will be applied to the [Palette.Builder] to customize the
 * generation of the [Palette].
 * @return A [PaletteState] that will be remembered across composition.
 */
@Composable
public fun rememberPlatformFilePaletteState(
    cacheSize: Int = PaletteState.DEFAULT_CACHE_SIZE,
    coroutineContext: CoroutineContext = Dispatchers.Default,
    builder: Palette.Builder.() -> Unit = {},
): PaletteState<PlatformFile> =
    rememberPaletteState(
        loader = PlatformFileLoader,
        cacheSize = cacheSize,
        coroutineContext = coroutineContext,
        builder = builder,
    )
