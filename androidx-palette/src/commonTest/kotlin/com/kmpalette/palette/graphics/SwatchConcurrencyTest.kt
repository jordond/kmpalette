package com.kmpalette.palette.graphics

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Regression tests for issue #121, "Alpha must be between 0 and 255".
 */
class SwatchConcurrencyTest {
    @Test
    fun textColors_areStableUnderConcurrentAccess() =
        runTest {
            withContext(Dispatchers.Default) {
                val swatches = List(COLOR_COUNT) { Palette.Swatch(rgbFor(it), population = 1) }

                // Compute the expected values first, uncontended.
                val expected = swatches.map { it.titleTextColor to it.bodyTextColor }

                repeat(ROUNDS) {
                    val actual =
                        coroutineScope {
                            List(COLOR_COUNT) { index ->
                                async {
                                    val swatch = Palette.Swatch(rgbFor(index), population = 1)
                                    swatch.titleTextColor to swatch.bodyTextColor
                                }
                            }.awaitAll()
                        }

                    assertEquals(expected, actual, "text colours must not depend on scheduling")
                }
            }
        }

    @Test
    fun hsl_isStableUnderConcurrentAccess() =
        runTest {
            withContext(Dispatchers.Default) {
                val expected = List(COLOR_COUNT) { swatchHsl(rgbFor(it)) }

                repeat(ROUNDS) {
                    val actual =
                        coroutineScope {
                            List(COLOR_COUNT) { index ->
                                async { swatchHsl(rgbFor(index)) }
                            }.awaitAll()
                        }

                    assertEquals(expected, actual, "HSL must not depend on scheduling")
                }
            }
        }

    @Test
    fun textColors_neverProduceAnOutOfRangeAlpha() {
        // Exhaustively walk the greyscale ramp plus the saturated primaries. Any swatch that
        // reaches the both-alphas-are-invalid branch would throw out of these getters.
        val colors =
            buildList {
                for (value in 0..255) add(argb(value, value, value))
                for (value in 0..255) {
                    add(argb(value, 0, 0))
                    add(argb(0, value, 0))
                    add(argb(0, 0, value))
                }
            }

        for (color in colors) {
            val swatch = Palette.Swatch(color, population = 1)
            val title = swatch.titleTextColor ushr 24
            val body = swatch.bodyTextColor ushr 24
            assertTrue(title in 0..255, "title alpha $title out of range for ${color.toString(16)}")
            assertTrue(body in 0..255, "body alpha $body out of range for ${color.toString(16)}")
        }
    }

    private fun swatchHsl(rgb: Int): List<Float> = Palette.Swatch(rgb, population = 1).hsl.toList()

    private companion object {
        const val COLOR_COUNT = 64
        const val ROUNDS = 25

        fun argb(
            red: Int,
            green: Int,
            blue: Int,
        ): Int = (0xFF shl 24) or (red shl 16) or (green shl 8) or blue

        fun rgbFor(index: Int): Int =
            argb(
                red = (index * 37) and 0xFF,
                green = (index * 91) and 0xFF,
                blue = (index * 143) and 0xFF,
            )
    }
}
