package dev.jordond.kmpalette

import androidx.compose.runtime.Composable
import dev.jordond.kmpalette.DominantColorState.Companion.DEFAULT_CACHE_SIZE
import dev.jordond.kmpalette.loader.Base64Loader
import dev.jordond.kmpalette.palette.graphics.Palette
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
    cacheSize: Int = DEFAULT_CACHE_SIZE,
    coroutineContext: CoroutineContext = Dispatchers.Default,
    builder: Palette.Builder.() -> Unit = {},
): PaletteState<String> = rememberPaletteState(
    loader = Base64Loader,
    cacheSize = cacheSize,
    coroutineContext = coroutineContext,
    builder = builder,
)
