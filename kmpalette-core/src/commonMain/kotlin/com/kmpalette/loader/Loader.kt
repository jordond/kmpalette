package com.kmpalette.loader

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.kmpalette.internal.PainterImage

/**
 * A default loader that takes an [ImageBitmap] and returns it.
 */
internal val ImageBitmapLoader = object : ImageBitmapLoader<ImageBitmap> {

    override suspend fun load(input: ImageBitmap): ImageBitmap = input
}

/**
 * A default loader that takes an [Painter], draws it as an [ImageBitmap] and returns that.
 */
public class PainterLoader(
    private val density: Density,
    private val layoutDirection: LayoutDirection
) : ImageBitmapLoader<Painter> {
    override suspend fun load(input: Painter): ImageBitmap {
        return PainterImage(input, density, layoutDirection).asBitmap()
    }
}