package com.kmpalette.internal

import kotlin.test.Test
import kotlin.test.assertEquals

class ScaledBitmapPixelsTest {
    @Test
    fun calculateScaledDimensions_smallerThanTarget_returnsOriginal() {
        val (width, height) = calculateScaledDimensions(100, 100, 112 * 112)
        assertEquals(100, width)
        assertEquals(100, height)
    }

    @Test
    fun calculateScaledDimensions_equalToTarget_returnsOriginal() {
        val (width, height) = calculateScaledDimensions(112, 112, 112 * 112)
        assertEquals(112, width)
        assertEquals(112, height)
    }

    @Test
    fun calculateScaledDimensions_largerThanTarget_scalesDown() {
        val (width, height) = calculateScaledDimensions(4000, 3000, 112 * 112)

        val scaledArea = width * height
        val targetArea = 112 * 112

        assertEquals(true, scaledArea <= targetArea + 200, "Scaled area $scaledArea should be close to target $targetArea")
        assertEquals(true, width > 0)
        assertEquals(true, height > 0)
    }

    @Test
    fun calculateScaledDimensions_preservesAspectRatio() {
        val originalWidth = 4000
        val originalHeight = 2000
        val originalRatio = originalWidth.toDouble() / originalHeight

        val (width, height) = calculateScaledDimensions(originalWidth, originalHeight, 112 * 112)
        val scaledRatio = width.toDouble() / height

        val ratioDiff = kotlin.math.abs(originalRatio - scaledRatio)
        assertEquals(true, ratioDiff < 0.1, "Aspect ratio should be preserved. Original: $originalRatio, Scaled: $scaledRatio")
    }

    @Test
    fun calculateScaledDimensions_veryLargeImage_scalesCorrectly() {
        val (width, height) = calculateScaledDimensions(10000, 10000, 112 * 112)

        assertEquals(true, width > 0)
        assertEquals(true, height > 0)
        assertEquals(true, width <= 112 + 1)
        assertEquals(true, height <= 112 + 1)
    }

    @Test
    fun calculateScaledDimensions_wideImage_scalesCorrectly() {
        val (width, height) = calculateScaledDimensions(5000, 100, 112 * 112)

        assertEquals(true, width > 0)
        assertEquals(true, height > 0)
        val scaledArea = width * height
        assertEquals(true, scaledArea <= 112 * 112 + 500, "Scaled area $scaledArea should be close to target")
    }

    @Test
    fun calculateScaledDimensions_tallImage_scalesCorrectly() {
        val (width, height) = calculateScaledDimensions(100, 5000, 112 * 112)

        assertEquals(true, width > 0)
        assertEquals(true, height > 0)
        val scaledArea = width * height
        assertEquals(true, scaledArea <= 112 * 112 + 500, "Scaled area $scaledArea should be close to target")
    }

    @Test
    fun calculateScaledDimensions_minimumDimensionIsOne() {
        val (width, height) = calculateScaledDimensions(1000000, 1, 100)

        assertEquals(true, width >= 1)
        assertEquals(true, height >= 1)
    }
}
