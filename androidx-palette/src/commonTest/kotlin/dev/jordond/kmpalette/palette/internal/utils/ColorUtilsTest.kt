package dev.jordond.kmpalette.palette.internal.utils

import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ColorUtilsTest {

    @Test
    fun `should convert the Color int to an HSL FloatArray`() {
        val color: Int = 0x80ABCDEF.toInt()
        val hsl = FloatArray(3).also { ColorUtils.colorToHSL(color, it) }
        assertEquals(210f, hsl[0], "hue should be 210")
        assertEquals(68, (hsl[1] * 100).toInt(), "saturation should be 68%")
        assertEquals(80, (hsl[2] * 100).toInt(), "lightness should be 80%")
    }

    @Test
    fun `should handle monochromatic colors when converting from RGB to HSL`() {
        val color: Int = 0x80FFFFFF.toInt()
        val hsl = FloatArray(3).also { ColorUtils.colorToHSL(color, it) }
        assertEquals(0f, hsl[0], "hue should be 0")
        assertEquals(0, (hsl[1] * 100).toInt(), "saturation should be 0%")
        assertEquals(100, (hsl[2] * 100).toInt(), "lightness should be 100%")
    }

    @Test
    fun `should set the alpha on a Color Int`() {
        val color: Int = 0x80ABCDEF.toInt()
        val newColor = ColorUtils.setAlpha(color, 0x40)
        assertEquals(0x40ABCDEF, newColor, "alpha should be 0x40")

        try {
            ColorUtils.setAlpha(color, 0x100)
            assertTrue(false, "should throw IllegalArgumentException")
        } catch (exception: IllegalArgumentException) {
            assertTrue(true, "should throw IllegalArgumentException")
        }

        try {
            ColorUtils.setAlpha(color, 0x300)
            assertTrue(false, "should throw IllegalArgumentException")
        } catch (exception: IllegalArgumentException) {
            assertTrue(true, "should throw IllegalArgumentException")
        }

        try {
            ColorUtils.setAlpha(color, -0x100)
            assertTrue(false, "should throw IllegalArgumentException")
        } catch (exception: IllegalArgumentException) {
            assertTrue(true, "should throw IllegalArgumentException")
        }
    }

    @Test
    fun `should calculate the luminance of a ColorInt`() {
        val color1: Int = 0x80ABCDEF.toInt()
        val luminance1 = ColorUtils.calculateLuminance(color1).roundTo(4)
        assertEquals(0.5855, luminance1, "luminance should be 0.5855")

        val color2: Int = 0x80FFFFFF.toInt()
        val luminance2 = ColorUtils.calculateLuminance(color2).roundTo(4)
        assertEquals(1.0, luminance2, "luminance should be 1.0")

        val color3: Int = 0x80000000.toInt()
        val luminance3 = ColorUtils.calculateLuminance(color3).roundTo(4)
        assertEquals(0.0, luminance3, "luminance should be 0.0")
    }

    @Test
    fun `should calculate the minimum alpha`() {
        val foreground = 0x20ABCDEF
        val background = 0xFFFEDCBA.toInt()

        val minAlpha = ColorUtils.calculateMinimumAlpha(foreground, background, 1.2f)
        assertEquals(185, minAlpha, "minAlpha should be 185")
    }

    @Test
    fun `should throw if color is not opaque when calculating minimum alpha`() {
        val color: Int = 0x80ABCDEF.toInt()
        try {
            ColorUtils.calculateMinimumAlpha(color, color, 1.2f)
            assertTrue(false, "should throw IllegalArgumentException")
        } catch (exception: IllegalArgumentException) {
            assertTrue(true, "should throw IllegalArgumentException")
        }
    }

    @Test
    fun `should return an invalid -1 when calculating minimum alpha if there is not sufficient contrast`() {
        val foreground = 0x20ABCDEF
        val background = 0xFFABCDEF.toInt()
        val minAlpha = ColorUtils.calculateMinimumAlpha(foreground, background, 1.2f)
        assertEquals(-1, minAlpha, "minAlpha should be -1")
    }

    @Test
    fun `should extract the alpha`() {
        val color: Int = 0x80FFFFFF.toInt()
        val alpha = ColorUtils.alpha(color)
        assertTrue(alpha == 0x80, "alpha should be 0x80")
    }

    @Test
    fun `should extract the red value from a Color Int`() {
        val color: Int = 0x80ABCDEF.toInt()
        val red = ColorUtils.red(color)
        assertTrue(red == 0xAB, "red should be 0xAB")
    }

    @Test
    fun `should extract the green value from a Color Int`() {
        val color: Int = 0x80ABCDEF.toInt()
        val green = ColorUtils.green(color)
        assertTrue(green == 0xCD, "green should be 0xCD")
    }

    @Test
    fun `should extract the blue value from a Color Int`() {
        val color: Int = 0x80ABCDEF.toInt()
        val blue = ColorUtils.blue(color)
        assertTrue(blue == 0xEF, "blue should be 0xEF")
    }

    @Test
    fun `should create a Color int from the individual values`() {
        val color: Int = ColorUtils.argb(0x80, 0xAB, 0xCD, 0xEF)
        assertEquals(0x80ABCDEF.toInt(), color, "color should be 0x80ABCDEF")
    }

    @Test
    fun `should create a Color int from the indvidual RGB values`() {
        val color: Int = ColorUtils.rgb(0xAB, 0xCD, 0xEF)
        assertEquals(0xFFABCDEF.toInt(), color, "color should be 0xFFABCDEF")
    }

    private fun Double.roundTo(n: Int): Double {
        return (this * 10.0.pow(n)).roundToInt() / 10.0.pow(n)
    }
}