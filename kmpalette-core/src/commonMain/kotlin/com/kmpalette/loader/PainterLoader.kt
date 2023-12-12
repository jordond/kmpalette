package com.kmpalette.loader

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.kmpalette.internal.PainterImage

/**
 * A default loader that takes an [Painter], draws it as an [ImageBitmap] and returns that.
 */
public class PainterLoader(
    private val density: Density,
    private val layoutDirection: LayoutDirection,
) : ImageBitmapLoader<Painter> {

    override suspend fun load(input: Painter): ImageBitmap {
        return PainterImage(input, density, layoutDirection).asBitmap()
    }
}

/**
 * Create and remember a [PainterLoader] that uses the [LocalDensity] and [LocalLayoutDirection]
 * to generate a Palette from a [Painter].
 *
 * @param[density] The [Density] to use when drawing the [Painter].
 * @param[layoutDirection] The [LayoutDirection] to use when drawing the [Painter].
 * @return A [PainterLoader] for the given [density] and [layoutDirection].
 */
@Composable
public fun rememberPainterLoader(
    density: Density = LocalDensity.current,
    layoutDirection: LayoutDirection = LocalLayoutDirection.current,
): PainterLoader = remember(density, layoutDirection) {
    PainterLoader(density, layoutDirection)
}