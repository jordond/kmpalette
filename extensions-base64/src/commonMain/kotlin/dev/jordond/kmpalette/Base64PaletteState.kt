package dev.jordond.kmpalette

import androidx.compose.runtime.Composable
import dev.jordond.kmpalette.DominantColorState.Companion.DEFAULT_CACHE_SIZE
import dev.jordond.kmpalette.loader.Base64Loader
import dev.jordond.kmpalette.palette.graphics.Palette
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext
import kotlin.io.encoding.ExperimentalEncodingApi

@ExperimentalEncodingApi
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
