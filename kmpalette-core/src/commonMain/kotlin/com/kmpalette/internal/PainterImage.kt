package com.kmpalette.internal

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.roundToInt

/**
 * Fallback edge length used when a [Painter] reports no usable intrinsic size.
 *
 * Painters such as `ColorPainter` and `BrushPainter`, and any async painter that has not finished
 * loading, report [Size.Unspecified]. There is no meaningful size to derive in that case, so the
 * painter is drawn into a square of this size instead. It is large enough for quantization to
 * produce a stable palette and small enough to stay cheap.
 */
private const val FALLBACK_SIZE = 128

internal class PainterImage(
    private val painter: Painter,
    private val density: Density,
    private val layoutDirection: LayoutDirection,
) {
    /**
     * Draws the painter into a new [ImageBitmap].
     *
     * When [width] or [height] are not supplied they are derived from [Painter.intrinsicSize],
     * falling back to [FALLBACK_SIZE] when that size is unspecified or non-positive. Reading
     * `intrinsicSize.width` directly throws for an unspecified size, and an infinite size cannot
     * be allocated, so both are handled here. See issue #197.
     */
    fun asBitmap(
        width: Int = intrinsicWidth(),
        height: Int = intrinsicHeight(),
    ): ImageBitmap {
        val bitmap = ImageBitmap(width, height)
        val canvas = Canvas(bitmap)
        val floatSize = Size(width.toFloat(), height.toFloat())

        bitmap.prepareToDraw()

        CanvasDrawScope().draw(
            density = density,
            layoutDirection = layoutDirection,
            canvas = canvas,
            size = floatSize,
        ) {
            with(painter) {
                draw(floatSize)
            }
        }

        return bitmap
    }

    private fun intrinsicWidth(): Int = painter.intrinsicSize.dimensionOrFallback { it.width }

    private fun intrinsicHeight(): Int = painter.intrinsicSize.dimensionOrFallback { it.height }

    private inline fun Size.dimensionOrFallback(select: (Size) -> Float): Int {
        if (!isSpecified) return FALLBACK_SIZE
        val value = select(this)
        if (!value.isFinite() || value < 1f) return FALLBACK_SIZE
        return value.roundToInt()
    }
}
