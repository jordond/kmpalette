package com.kmpalette.extensions.resource

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.kmpalette.PaletteState
import com.kmpalette.loader.rememberDrawableLoader
import com.kmpalette.palette.graphics.Palette
import com.kmpalette.rememberPaletteState
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * Wrapper around [rememberPaletteState] that uses [com.kmpalette.loader.DrawableLoader] to load the image.
 *
 * @see rememberPaletteState
 * @param[cacheSize] The maximum number of [Palette]s to cache. If 0, no caching will be done.
 * @param[coroutineContext] The [CoroutineContext] to use for generating [Palette]s.
 * @param[context] The [Context] to use for loading the Android drawable resource.
 * @param[builder] A lambda that will be applied to the [Palette.Builder] to customize the
 * generation of the [Palette].
 * @return A [PaletteState] that will be remembered across composition.
 */
@Composable
public fun rememberDrawablePaletteState(
    cacheSize: Int = PaletteState.DEFAULT_CACHE_SIZE,
    coroutineContext: CoroutineContext = Dispatchers.Default,
    context: Context = LocalContext.current,
    builder: Palette.Builder.() -> Unit = {},
): PaletteState<Int> = rememberPaletteState(
    loader = rememberDrawableLoader(context = context),
    cacheSize = cacheSize,
    coroutineContext = coroutineContext,
    builder = builder,
)