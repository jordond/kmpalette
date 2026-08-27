package com.kmpalette.internal

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Regression tests for issue #197.
 *
 * A [Painter] is free to report [Size.Unspecified], and reading `width` off an unspecified [Size]
 * throws `IllegalStateException("Size is unspecified")`. `PainterImage` used to do exactly that
 * while computing its default bitmap dimensions.
 *
 * Lives in jvmTest rather than commonTest because allocating an [ImageBitmap] needs a working
 * Skiko runtime, and the karma bundle used for the browser test targets does not load one.
 * The logic under test is pure common Kotlin, so the JVM run covers every platform.
 */
class PainterImageTest {
    private val density = Density(density = 1f, fontScale = 1f)

    private class FixedSizePainter(
        override val intrinsicSize: Size,
    ) : Painter() {
        override fun DrawScope.onDraw() {
            drawIntoCanvas { }
        }
    }

    private fun bitmapFor(size: Size) =
        PainterImage(
            painter = FixedSizePainter(size),
            density = density,
            layoutDirection = LayoutDirection.Ltr,
        ).asBitmap()

    @Test
    fun asBitmap_intrinsicSizeUnspecified_doesNotThrow() {
        val bitmap = bitmapFor(Size.Unspecified)

        assertTrue(bitmap.width > 0, "width should fall back to a usable value")
        assertTrue(bitmap.height > 0, "height should fall back to a usable value")
    }

    @Test
    fun asBitmap_intrinsicSizeInfinite_doesNotThrow() {
        val bitmap = bitmapFor(Size(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY))

        assertTrue(bitmap.width > 0)
        assertTrue(bitmap.height > 0)
    }

    @Test
    fun asBitmap_intrinsicSizeZero_fallsBack() {
        val bitmap = bitmapFor(Size.Zero)

        assertTrue(bitmap.width > 0, "a zero-width bitmap cannot be quantized")
        assertTrue(bitmap.height > 0)
    }

    @Test
    fun asBitmap_intrinsicSizeSpecified_isUsedVerbatim() {
        val bitmap = bitmapFor(Size(24f, 42f))

        assertEquals(24, bitmap.width)
        assertEquals(42, bitmap.height)
    }

    @Test
    fun asBitmap_explicitDimensions_overrideIntrinsicSize() {
        val bitmap =
            PainterImage(
                painter = FixedSizePainter(Size.Unspecified),
                density = density,
                layoutDirection = LayoutDirection.Ltr,
            ).asBitmap(width = 8, height = 9)

        assertEquals(8, bitmap.width)
        assertEquals(9, bitmap.height)
    }

    @Test
    fun asBitmap_unspecifiedSizePainter_stillDraws() {
        // ColorPainter reports Size.Unspecified, which is the exact shape reported in #197.
        val painter =
            object : Painter() {
                override val intrinsicSize: Size = Size.Unspecified

                override fun DrawScope.onDraw() {
                    drawRect(Color.Red)
                }
            }

        val bitmap = PainterImage(painter, density, LayoutDirection.Ltr).asBitmap()
        val pixels = IntArray(bitmap.width * bitmap.height)
        bitmap.readPixels(pixels)

        assertTrue(pixels.isNotEmpty())
        assertTrue(pixels.all { it == pixels[0] }, "a solid fill should produce one colour")
    }
}
