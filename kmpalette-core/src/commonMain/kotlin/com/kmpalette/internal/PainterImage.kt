package com.kmpalette.internal

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.roundToInt

internal class PainterImage(
    private val painter: Painter,
    private val density: Density,
    private val layoutDirection: LayoutDirection
) {
    fun asBitmap(
        width: Int = painter.intrinsicSize.width.roundToInt(),
        height: Int = painter.intrinsicSize.height.roundToInt()
    ): ImageBitmap {
        val bitmap = ImageBitmap(width, height)
        val canvas = Canvas(bitmap)
        val floatSize = Size(width.toFloat(), height.toFloat())

        bitmap.prepareToDraw()

        CanvasDrawScope().draw(
            density = density,
            layoutDirection = layoutDirection,
            canvas = canvas,
            size = floatSize
        ) {
            with(painter) {
                draw(floatSize)
            }
        }

        return bitmap
    }
}