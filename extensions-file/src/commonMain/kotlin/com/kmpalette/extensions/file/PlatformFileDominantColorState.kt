package com.kmpalette.extensions.file

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import com.kmpalette.DominantColorState
import com.kmpalette.loader.PlatformFileLoader
import com.kmpalette.palette.graphics.Palette
import com.kmpalette.rememberDominantColorState
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * Wrapper around [rememberDominantColorState] that uses [PlatformFileLoader] to load the image.
 *
 * @see rememberDominantColorState
 * @param[defaultColor] The default color, which will be used if [Palette.generate] fails.
 * @param[defaultOnColor] The default color to use _on_ [defaultColor].
 * @param[cacheSize] The size of the LruCache used to store recent results. Pass `0` to disable.
 * @param[coroutineContext] The [CoroutineContext] used to launch the coroutine.
 * @param[isSwatchValid] A lambda which allows filtering of the calculated [Palette.Swatch].
 * @param[builder] A lambda which allows customization of the [Palette.Builder] used to generate
 * the [Palette].
 * @return A [DominantColorState] which can be used to generate a dominant color
 * from a [ImageBitmap].
 */
@Composable
public fun rememberPlatformFileDominantColorState(
    defaultColor: Color,
    defaultOnColor: Color,
    cacheSize: Int = DominantColorState.DEFAULT_CACHE_SIZE,
    coroutineContext: CoroutineContext = Dispatchers.Default,
    isSwatchValid: (Palette.Swatch) -> Boolean = { true },
    builder: Palette.Builder.() -> Unit = {},
): DominantColorState<PlatformFile> = rememberDominantColorState(
    loader = PlatformFileLoader,
    defaultColor = defaultColor,
    defaultOnColor = defaultOnColor,
    cacheSize = cacheSize,
    coroutineContext = coroutineContext,
    isSwatchValid = isSwatchValid,
    builder = builder,
)