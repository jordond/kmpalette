package dev.jordond.kmpalette.palette.utils

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
        assertEquals(hsl[0], 210f, "hue should be 210")
        assertEquals((hsl[1] * 100).toInt(), 68, "saturation should be 68%")
        assertEquals((hsl[2] * 100).toInt(), 80, "lightness should be 80%")
    }

    @Test
    fun `should set the alpha on a Color Int`() {
        val color: Int = 0x80ABCDEF.toInt()
        val newColor = ColorUtils.setAlpha(color, 0x40)
        assertEquals(newColor, 0x40ABCDEF, "alpha should be 0x40")

        try {
            ColorUtils.setAlpha(color, 0x100)
            assertTrue(false, "should throw IllegalArgumentException")
        } catch (exception: IllegalArgumentException) {
            assertTrue(true, "should throw IllegalArgumentException")
        }
    }

    @Test
    fun `should calculate the luminance of a ColorInt`() {
        val color1: Int = 0x80ABCDEF.toInt()
        val luminance1 = ColorUtils.calculateLuminance(color1).roundTo(4)
        assertEquals(luminance1, 0.5855, "luminance should be 0.5855")

        val color2: Int = 0x80FFFFFF.toInt()
        val luminance2 = ColorUtils.calculateLuminance(color2).roundTo(4)
        assertEquals(luminance2, 1.0, "luminance should be 1.0")

        val color3: Int = 0x80000000.toInt()
        val luminance3 = ColorUtils.calculateLuminance(color3).roundTo(4)
        assertEquals(luminance3, 0.0, "luminance should be 0.0")
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
        assertEquals(color, 0x80ABCDEF.toInt(), "color should be 0x80ABCDEF")
    }

    @Test
    fun `should create a Color int from the indvidual RGB values`() {
        val color: Int = ColorUtils.rgb(0xAB, 0xCD, 0xEF)
        assertEquals(color, 0xFFABCDEF.toInt(), "color should be 0xFFABCDEF")
    }

    private fun Double.roundTo(n: Int): Double {
        return (this * 10.0.pow(n)).roundToInt() / 10.0.pow(n)
    }
}