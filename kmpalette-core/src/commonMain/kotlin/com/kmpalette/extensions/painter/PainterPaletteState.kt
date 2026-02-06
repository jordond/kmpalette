package com.kmpalette.extensions.painter

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.kmpalette.PaletteState
import com.kmpalette.loader.PainterLoader
import com.kmpalette.loader.rememberPainterLoader
import com.kmpalette.palette.graphics.Palette
import com.kmpalette.rememberPaletteState
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * Wrapper around [rememberPaletteState] that uses [PainterLoader] to load the image.
 *
 * @see rememberPaletteState
 * @param[cacheSize] The maximum number of [Palette]s to cache. If 0, no caching will be done.
 * @param[coroutineContext] The [CoroutineContext] to use for generating [Palette]s.
 * @param[density] The [Density] used for drawing the [Painter] as [ImageBitmap].
 * @param[layoutDirection] The [LayoutDirection] used for drawing the [Painter] as [ImageBitmap].
 * @param[builder] A lambda that will be applied to the [Palette.Builder] to customize the
 * generation of the [Palette].
 * @return A [PaletteState] that will be remembered across composition.
 */
@Composable
public fun rememberPainterPaletteState(
    cacheSize: Int = PaletteState.DEFAULT_CACHE_SIZE,
    coroutineContext: CoroutineContext = Dispatchers.Default,
    density: Density = LocalDensity.current,
    layoutDirection: LayoutDirection = LocalLayoutDirection.current,
    builder: Palette.Builder.() -> Unit = {},
): PaletteState<Painter> = rememberPaletteState(
    loader = rememberPainterLoader(density, layoutDirection),
    cacheSize = cacheSize,
    coroutineContext = coroutineContext,
    builder = builder,
)
