package dev.jordond.kmpalette

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import dev.jordond.kmpalette.DominantColorState.Companion.DEFAULT_CACHE_SIZE
import dev.jordond.kmpalette.loader.ByteArrayLoader
import dev.jordond.kmpalette.palette.graphics.Palette
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

@Composable
public fun rememberByteArrayDominantColorState(
    defaultColor: Color,
    defaultOnColor: Color,
    cacheSize: Int = DEFAULT_CACHE_SIZE,
    coroutineContext: CoroutineContext = Dispatchers.Default,
    isColorValid: (Color) -> Boolean = { true },
    builder: Palette.Builder.() -> Unit = {},
): DominantColorState<ByteArray> = rememberDominantColorState(
    loader = ByteArrayLoader,
    defaultColor = defaultColor,
    defaultOnColor = defaultOnColor,
    cacheSize = cacheSize,
    coroutineContext = coroutineContext,
    isColorValid = isColorValid,
    builder = builder,
)