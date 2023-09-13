package dev.jordond.kmpalette

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import dev.jordond.kmpalette.loader.LibresLoader
import dev.jordond.kmpalette.palette.graphics.Palette
import io.github.skeptick.libres.images.Image
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

@Composable
public fun rememberLibresDominantColorState(
    defaultColor: Color,
    defaultOnColor: Color,
    cacheSize: Int = DominantColorState.DEFAULT_CACHE_SIZE,
    coroutineContext: CoroutineContext = Dispatchers.Default,
    isColorValid: (Color) -> Boolean = { true },
    builder: Palette.Builder.() -> Unit = {},
): DominantColorState<Image> = rememberDominantColorState(
    loader = LibresLoader,
    defaultColor = defaultColor,
    defaultOnColor = defaultOnColor,
    cacheSize = cacheSize,
    coroutineContext = coroutineContext,
    isColorValid = isColorValid,
    builder = builder,
)
