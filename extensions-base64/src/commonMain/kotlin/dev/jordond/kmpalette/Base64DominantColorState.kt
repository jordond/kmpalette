package dev.jordond.kmpalette

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import dev.jordond.kmpalette.DominantColorState.Companion.DEFAULT_CACHE_SIZE
import dev.jordond.kmpalette.loader.Base64Loader
import dev.jordond.kmpalette.palette.graphics.Palette
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext
import kotlin.io.encoding.ExperimentalEncodingApi

@ExperimentalEncodingApi
@Composable
public fun rememberBase64DominantColorState(
    defaultColor: Color,
    defaultOnColor: Color,
    cacheSize: Int = DEFAULT_CACHE_SIZE,
    coroutineContext: CoroutineContext = Dispatchers.Default,
    isColorValid: (Color) -> Boolean = { true },
    builder: Palette.Builder.() -> Unit = {},
): DominantColorState<String> = rememberDominantColorState(
    loader = Base64Loader,
    defaultColor = defaultColor,
    defaultOnColor = defaultOnColor,
    cacheSize = cacheSize,
    coroutineContext = coroutineContext,
    isColorValid = isColorValid,
    builder = builder,
)