package dev.jordond.kmpalette

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import dev.jordond.kmpalette.dominant.DominantColorState
import dev.jordond.kmpalette.dominant.DominantColorState.Companion.DEFAULT_CACHE_SIZE
import dev.jordond.kmpalette.loader.ByteArrayLoader
import dev.jordond.kmpalette.loader.ImageBitmapLoader
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
): ByteArrayDominantColorState = remember {
    ByteArrayDominantColorState(
        defaultColor = defaultColor,
        defaultOnColor = defaultOnColor,
        cacheSize = cacheSize,
        coroutineContext = coroutineContext,
        isColorValid = isColorValid,
        builder = builder,
    )
}

@Stable
public class ByteArrayDominantColorState(
    defaultColor: Color,
    defaultOnColor: Color,
    cacheSize: Int = DEFAULT_CACHE_SIZE,
    coroutineContext: CoroutineContext = Dispatchers.Default,
    isColorValid: (Color) -> Boolean = { true },
    builder: Palette.Builder.() -> Unit = {},
) : DominantColorState<ByteArray>(
    defaultColor = defaultColor,
    defaultOnColor = defaultOnColor,
    cacheSize = cacheSize,
    coroutineContext = coroutineContext,
    isColorValid = isColorValid,
    builder = builder,
) {

    override val loader: ImageBitmapLoader<ByteArray> = ByteArrayLoader
}