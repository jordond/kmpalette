package com.kmpalette.palette.graphics

import kotlin.math.pow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

/**
 * Tests for Palette.Swatch functionality including HSL conversion and text colors.
 */
class SwatchTest {
    object Colors {
        const val RED = 0xFFFF0000.toInt()
        const val GREEN = 0xFF00FF00.toInt()
        const val BLUE = 0xFF0000FF.toInt()
        const val WHITE = 0xFFFFFFFF.toInt()
        const val BLACK = 0xFF000000.toInt()
        const val YELLOW = 0xFFFFFF00.toInt()
        const val CYAN = 0xFF00FFFF.toInt()
        const val MAGENTA = 0xFFFF00FF.toInt()
        const val GRAY = 0xFF808080.toInt()
    }

    // ==================== Swatch Creation Tests ====================

    @Test
    fun `swatch stores rgb and population correctly`() {
        val swatch = Palette.Swatch(Colors.RED, 500)

        assertEquals(Colors.RED, swatch.rgb)
        assertEquals(500, swatch.population)
    }

    @Test
    fun `swatch with zero population is valid`() {
        val swatch = Palette.Swatch(Colors.BLUE, 0)

        assertEquals(Colors.BLUE, swatch.rgb)
        assertEquals(0, swatch.population)
    }

    // ==================== HSL Conversion Tests ====================

    @Test
    fun `red swatch has correct hsl values`() {
        val swatch = Palette.Swatch(Colors.RED, 100)
        val hsl = swatch.hsl

        assertEquals(3, hsl.size)
        // Red: hue=0, saturation=1.0, lightness=0.5
        assertEquals(0f, hsl[0], 1f) // Hue (allow for floating point)
        assertEquals(1.0f, hsl[1], 0.01f) // Saturation
        assertEquals(0.5f, hsl[2], 0.01f) // Lightness
    }

    @Test
    fun `green swatch has correct hsl values`() {
        val swatch = Palette.Swatch(Colors.GREEN, 100)
        val hsl = swatch.hsl

        // Green: hue=120, saturation=1.0, lightness=0.5
        assertEquals(120f, hsl[0], 1f)
        assertEquals(1.0f, hsl[1], 0.01f)
        assertEquals(0.5f, hsl[2], 0.01f)
    }

    @Test
    fun `blue swatch has correct hsl values`() {
        val swatch = Palette.Swatch(Colors.BLUE, 100)
        val hsl = swatch.hsl

        // Blue: hue=240, saturation=1.0, lightness=0.5
        assertEquals(240f, hsl[0], 1f)
        assertEquals(1.0f, hsl[1], 0.01f)
        assertEquals(0.5f, hsl[2], 0.01f)
    }

    @Test
    fun `yellow swatch has correct hsl values`() {
        val swatch = Palette.Swatch(Colors.YELLOW, 100)
        val hsl = swatch.hsl

        // Yellow: hue=60, saturation=1.0, lightness=0.5
        assertEquals(60f, hsl[0], 1f)
        assertEquals(1.0f, hsl[1], 0.01f)
        assertEquals(0.5f, hsl[2], 0.01f)
    }

    @Test
    fun `white swatch has correct hsl values`() {
        val swatch = Palette.Swatch(Colors.WHITE, 100)
        val hsl = swatch.hsl

        // White: any hue, saturation=0, lightness=1.0
        assertEquals(0f, hsl[1], 0.01f) // Saturation is 0
        assertEquals(1.0f, hsl[2], 0.01f) // Lightness is 1.0
    }

    @Test
    fun `black swatch has correct hsl values`() {
        val swatch = Palette.Swatch(Colors.BLACK, 100)
        val hsl = swatch.hsl

        // Black: any hue, saturation=0, lightness=0
        assertEquals(0f, hsl[1], 0.01f) // Saturation is 0
        assertEquals(0f, hsl[2], 0.01f) // Lightness is 0
    }

    @Test
    fun `gray swatch has zero saturation`() {
        val swatch = Palette.Swatch(Colors.GRAY, 100)
        val hsl = swatch.hsl

        // Gray: any hue, saturation=0, lightness~0.5
        assertEquals(0f, hsl[1], 0.01f) // Saturation is 0
        assertTrue(hsl[2] > 0.4f && hsl[2] < 0.6f) // Lightness ~0.5
    }

    // ==================== Text Color Tests ====================

    @Test
    fun `dark color swatch provides light title text`() {
        // Dark blue background should have light text
        val darkBlue = 0xFF000080.toInt()
        val swatch = Palette.Swatch(darkBlue, 100)

        val titleColor = swatch.titleTextColor
        // Title text should be light (high luminance)
        val luminance = calculateLuminance(titleColor)
        assertTrue(luminance > 0.5f, "Title text on dark background should be light")
    }

    @Test
    fun `light color swatch provides dark title text`() {
        // Light yellow background should have dark text
        val lightYellow = 0xFFFFFF80.toInt()
        val swatch = Palette.Swatch(lightYellow, 100)

        val titleColor = swatch.titleTextColor
        // Title text should be dark (low luminance)
        val luminance = calculateLuminance(titleColor)
        assertTrue(luminance < 0.5f, "Title text on light background should be dark")
    }

    @Test
    fun `dark color swatch provides light body text`() {
        val darkGreen = 0xFF008000.toInt()
        val swatch = Palette.Swatch(darkGreen, 100)

        val bodyColor = swatch.bodyTextColor
        val luminance = calculateLuminance(bodyColor)
        assertTrue(luminance > 0.5f, "Body text on dark background should be light")
    }

    @Test
    fun `light color swatch provides dark body text`() {
        val lightPink = 0xFFFFCCCC.toInt()
        val swatch = Palette.Swatch(lightPink, 100)

        val bodyColor = swatch.bodyTextColor
        val luminance = calculateLuminance(bodyColor)
        assertTrue(luminance < 0.5f, "Body text on light background should be dark")
    }

    @Test
    fun `title and body text colors may differ`() {
        // For some colors, title and body might need different contrast levels
        val swatch = Palette.Swatch(Colors.GRAY, 100)

        // They should both be valid colors (not crashing)
        val titleColor = swatch.titleTextColor
        val bodyColor = swatch.bodyTextColor

        // Both should be opaque or semi-opaque
        val titleAlpha = (titleColor shr 24) and 0xFF
        val bodyAlpha = (bodyColor shr 24) and 0xFF
        assertTrue(titleAlpha > 0, "Title text should have alpha")
        assertTrue(bodyAlpha > 0, "Body text should have alpha")
    }

    // ==================== Equality Tests ====================

    @Test
    fun `swatches with same rgb and population are equal`() {
        val swatch1 = Palette.Swatch(Colors.RED, 100)
        val swatch2 = Palette.Swatch(Colors.RED, 100)

        assertEquals(swatch1, swatch2)
        assertEquals(swatch1.hashCode(), swatch2.hashCode())
    }

    @Test
    fun `swatches with different rgb are not equal`() {
        val swatch1 = Palette.Swatch(Colors.RED, 100)
        val swatch2 = Palette.Swatch(Colors.BLUE, 100)

        assertNotEquals(swatch1, swatch2)
    }

    @Test
    fun `swatches with different population are not equal`() {
        val swatch1 = Palette.Swatch(Colors.RED, 100)
        val swatch2 = Palette.Swatch(Colors.RED, 200)

        assertNotEquals(swatch1, swatch2)
    }

    // ==================== Helper Functions ====================

    /**
     * Simple luminance calculation for test verification.
     * Uses the relative luminance formula from WCAG.
     */
    private fun calculateLuminance(color: Int): Float {
        val r = ((color shr 16) and 0xFF) / 255f
        val g = ((color shr 8) and 0xFF) / 255f
        val b = (color and 0xFF) / 255f

        fun linearize(value: Float): Float =
            if (value <= 0.03928f) {
                value / 12.92f
            } else {
                ((value + 0.055) / 1.055).pow(2.4).toFloat()
            }

        return 0.2126f * linearize(r) + 0.7152f * linearize(g) + 0.0722f * linearize(b)
    }
}
