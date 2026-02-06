package com.kmpalette.palette.graphics

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Regression tests for Palette core functionality.
 * These tests use Palette.from(swatches) to verify algorithm behavior
 * without depending on ImageBitmap.
 */
class PaletteTest {
    object Colors {
        const val RED = 0xFFFF0000.toInt()
        const val GREEN = 0xFF00FF00.toInt()
        const val BLUE = 0xFF0000FF.toInt()
        const val WHITE = 0xFFFFFFFF.toInt()
        const val BLACK = 0xFF000000.toInt()
        const val YELLOW = 0xFFFFFF00.toInt()
        const val CYAN = 0xFF00FFFF.toInt()
        const val MAGENTA = 0xFFFF00FF.toInt()
        const val ORANGE = 0xFFFF8000.toInt()
        const val PURPLE = 0xFF800080.toInt()
        const val GRAY = 0xFF808080.toInt()
        const val LIGHT_GRAY = 0xFFCCCCCC.toInt()
        const val DARK_GRAY = 0xFF333333.toInt()
    }

    // ==================== Palette Creation Tests ====================

    @Test
    fun `from swatches creates valid palette`() {
        val swatches =
            listOf(
                Palette.Swatch(Colors.RED, 100),
                Palette.Swatch(Colors.GREEN, 200),
                Palette.Swatch(Colors.BLUE, 50),
            )
        val palette = Palette.from(swatches)

        assertEquals(3, palette.swatches.size)
        assertEquals(swatches, palette.swatches)
    }

    @Test
    fun `dominant swatch has highest population`() {
        val swatches =
            listOf(
                Palette.Swatch(Colors.RED, 100),
                Palette.Swatch(Colors.GREEN, 500),
                Palette.Swatch(Colors.BLUE, 200),
            )
        val palette = Palette.from(swatches)

        assertNotNull(palette.dominantSwatch)
        assertEquals(Colors.GREEN, palette.dominantSwatch!!.rgb)
        assertEquals(500, palette.dominantSwatch!!.population)
    }

    @Test
    fun `single swatch palette has that swatch as dominant`() {
        val swatches = listOf(Palette.Swatch(Colors.BLUE, 100))
        val palette = Palette.from(swatches)

        assertNotNull(palette.dominantSwatch)
        assertEquals(Colors.BLUE, palette.dominantSwatch!!.rgb)
    }

    // ==================== Get Color Tests ====================

    @Test
    fun `getDominantColor returns swatch color when available`() {
        val swatches =
            listOf(
                Palette.Swatch(Colors.RED, 100),
                Palette.Swatch(Colors.GREEN, 500),
            )
        val palette = Palette.from(swatches)

        assertEquals(Colors.GREEN, palette.getDominantColor(Colors.BLACK))
    }

    @Test
    fun `getVibrantColor returns default when no vibrant swatch`() {
        // Gray is not vibrant (low saturation)
        val swatches = listOf(Palette.Swatch(Colors.GRAY, 100))
        val palette = Palette.from(swatches)
        val defaultColor = 0xFF123456.toInt()

        assertEquals(defaultColor, palette.getVibrantColor(defaultColor))
    }

    @Test
    fun `getMutedColor returns default when no muted swatch`() {
        // Pure red is vibrant, not muted
        val swatches = listOf(Palette.Swatch(Colors.RED, 100))
        val palette = Palette.from(swatches)
        val defaultColor = 0xFF123456.toInt()

        assertEquals(defaultColor, palette.getMutedColor(defaultColor))
    }

    @Test
    fun `getLightVibrantColor returns default when unavailable`() {
        val swatches = listOf(Palette.Swatch(Colors.DARK_GRAY, 100))
        val palette = Palette.from(swatches)
        val defaultColor = 0xFFABCDEF.toInt()

        assertEquals(defaultColor, palette.getLightVibrantColor(defaultColor))
    }

    @Test
    fun `getDarkVibrantColor returns default when unavailable`() {
        val swatches = listOf(Palette.Swatch(Colors.LIGHT_GRAY, 100))
        val palette = Palette.from(swatches)
        val defaultColor = 0xFFABCDEF.toInt()

        assertEquals(defaultColor, palette.getDarkVibrantColor(defaultColor))
    }

    @Test
    fun `getLightMutedColor returns default when unavailable`() {
        val swatches = listOf(Palette.Swatch(Colors.RED, 100))
        val palette = Palette.from(swatches)
        val defaultColor = 0xFFABCDEF.toInt()

        assertEquals(defaultColor, palette.getLightMutedColor(defaultColor))
    }

    @Test
    fun `getDarkMutedColor returns default when unavailable`() {
        val swatches = listOf(Palette.Swatch(Colors.WHITE, 100))
        val palette = Palette.from(swatches)
        val defaultColor = 0xFFABCDEF.toInt()

        assertEquals(defaultColor, palette.getDarkMutedColor(defaultColor))
    }

    // ==================== Swatch Target Tests ====================

    @Test
    fun `getSwatchForTarget returns null when no targets added`() {
        val swatches = listOf(Palette.Swatch(Colors.RED, 100))
        val palette = Palette.from(swatches)

        assertNull(palette.getSwatchForTarget(Target.VIBRANT))
        assertNull(palette.vibrantSwatch)
        assertNull(palette.mutedSwatch)
    }

    @Test
    fun `getColorForTarget returns default when no targets added`() {
        val swatches = listOf(Palette.Swatch(Colors.GRAY, 100))
        val palette = Palette.from(swatches)
        val defaultColor = 0xFFDEADBE.toInt()

        assertEquals(defaultColor, palette.getColorForTarget(Target.VIBRANT, defaultColor))
    }

    // ==================== Filter Tests ====================

    @Test
    fun `DEFAULT_FILTER rejects near-white colors`() {
        val nearWhite = 0xFFFEFEFE.toInt()
        val hsl = floatArrayOf(0f, 0f, 0.99f)

        val isAllowed = Palette.DEFAULT_FILTER.isAllowed(nearWhite, hsl)
        assertEquals(false, isAllowed)
    }

    @Test
    fun `DEFAULT_FILTER rejects near-black colors`() {
        val nearBlack = 0xFF010101.toInt()
        val hsl = floatArrayOf(0f, 0f, 0.01f)

        val isAllowed = Palette.DEFAULT_FILTER.isAllowed(nearBlack, hsl)
        assertEquals(false, isAllowed)
    }

    @Test
    fun `DEFAULT_FILTER accepts vibrant colors`() {
        // Pure red: hue=0, saturation=1.0, lightness=0.5
        val hsl = floatArrayOf(0f, 1.0f, 0.5f)

        val isAllowed = Palette.DEFAULT_FILTER.isAllowed(Colors.RED, hsl)
        assertEquals(true, isAllowed)
    }

    @Test
    fun `DEFAULT_FILTER accepts medium gray`() {
        // Medium gray: hue=0, saturation=0, lightness=0.5
        val hsl = floatArrayOf(0f, 0f, 0.5f)

        val isAllowed = Palette.DEFAULT_FILTER.isAllowed(Colors.GRAY, hsl)
        assertEquals(true, isAllowed)
    }

    // ==================== Constants Tests ====================

    @Test
    fun `default constants are correct`() {
        assertEquals(112 * 112, Palette.DEFAULT_RESIZE_BITMAP_AREA)
        assertEquals(16, Palette.DEFAULT_CALCULATE_NUMBER_COLORS)
        assertEquals(3.0f, Palette.MIN_CONTRAST_TITLE_TEXT)
        assertEquals(4.5f, Palette.MIN_CONTRAST_BODY_TEXT)
    }

    @Test
    fun `DEFAULT_FILTER is not null`() {
        assertNotNull(Palette.DEFAULT_FILTER)
    }

    // ==================== Builder Configuration Tests ====================

    @Test
    fun `scaling false disables internal scaling`() {
        val pixels = IntArray(10 * 10) { Colors.RED }
        val palette =
            Palette
                .from(pixels, 10, 10)
                .scaling(false)
                .clearFilters()
                .generate()

        assertTrue(palette.swatches.isNotEmpty())
    }

    @Test
    fun `scaling true enables internal scaling`() {
        val pixels = IntArray(10 * 10) { Colors.BLUE }
        val palette =
            Palette
                .from(pixels, 10, 10)
                .scaling(true)
                .clearFilters()
                .generate()

        assertTrue(palette.swatches.isNotEmpty())
    }

    // ==================== Population-Based Selection Tests ====================

    @Test
    fun `higher population swatch wins for same target match`() {
        // Both are vibrant colors, but green has higher population
        val swatches =
            listOf(
                Palette.Swatch(Colors.RED, 100),
                Palette.Swatch(Colors.GREEN, 500),
                Palette.Swatch(Colors.BLUE, 200),
            )
        val palette = Palette.from(swatches)

        // Dominant should be green (highest population)
        assertNotNull(palette.dominantSwatch)
        assertEquals(Colors.GREEN, palette.dominantSwatch!!.rgb)
    }

    @Test
    fun `swatches list preserves order`() {
        val swatches =
            listOf(
                Palette.Swatch(Colors.RED, 100),
                Palette.Swatch(Colors.GREEN, 200),
                Palette.Swatch(Colors.BLUE, 300),
            )
        val palette = Palette.from(swatches)

        assertEquals(Colors.RED, palette.swatches[0].rgb)
        assertEquals(Colors.GREEN, palette.swatches[1].rgb)
        assertEquals(Colors.BLUE, palette.swatches[2].rgb)
    }

    // ==================== Edge Cases ====================

    @Test
    fun `empty swatches list throws exception`() {
        try {
            Palette.from(emptyList())
            assertTrue(false, "Should have thrown IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            assertTrue(true)
        }
    }

    @Test
    fun `palette with many swatches works correctly`() {
        val swatches =
            (1..100).map { i ->
                // Create colors with varying populations
                val color = 0xFF000000.toInt() or (i shl 16) or (i shl 8) or i
                Palette.Swatch(color, i * 10)
            }
        val palette = Palette.from(swatches)

        assertEquals(100, palette.swatches.size)
        assertNotNull(palette.dominantSwatch)
        // Highest population should be last swatch (100 * 10 = 1000)
        assertEquals(1000, palette.dominantSwatch!!.population)
    }

    @Test
    fun `palette handles duplicate colors`() {
        val swatches =
            listOf(
                Palette.Swatch(Colors.RED, 100),
                Palette.Swatch(Colors.RED, 200), // Same color, different population
                Palette.Swatch(Colors.BLUE, 50),
            )
        val palette = Palette.from(swatches)

        assertEquals(3, palette.swatches.size)
        // Dominant should be the red with higher population
        assertEquals(200, palette.dominantSwatch!!.population)
    }
}
