package dev.jordond.kmpalette

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import dev.jordond.kmpalette.DominantColorState.Companion.DEFAULT_CACHE_SIZE
import dev.jordond.kmpalette.loader.ByteArrayLoader
import dev.jordond.kmpalette.palette.graphics.Palette
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * Wrapper around [rememberDominantColorState] that uses [ByteArrayLoader] to load the image.
 *
 * @see rememberDominantColorState
 * @param[defaultColor] The default color, which will be used if [Palette.generate] fails.
 * @param[defaultOnColor] The default color to use _on_ [defaultColor].
 * @param[cacheSize] The size of the LruCache used to store recent results. Pass `0` to disable.
 * @param[coroutineContext] The [CoroutineContext] used to launch the coroutine.
 * @param[isColorValid] A lambda which allows filtering of the calculated image colors.
 * @param[builder] A lambda which allows customization of the [Palette.Builder] used to generate
 * the [Palette].
 * @return A [DominantColorState] which can be used to generate a dominant color
 * from a [ImageBitmap].
 */
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