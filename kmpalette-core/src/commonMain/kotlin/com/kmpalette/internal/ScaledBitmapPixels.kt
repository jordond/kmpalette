package com.kmpalette.internal

import androidx.compose.ui.graphics.ImageBitmap
import kotlin.math.ceil
import kotlin.math.sqrt

internal data class ScaledBitmapPixels(
    val pixels: IntArray,
    val width: Int,
    val height: Int,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as ScaledBitmapPixels
        return width == other.width && height == other.height && pixels.contentEquals(other.pixels)
    }

    override fun hashCode(): Int {
        var result = pixels.contentHashCode()
        result = 31 * result + width
        result = 31 * result + height
        return result
    }
}

internal fun calculateScaledDimensions(
    width: Int,
    height: Int,
    targetArea: Int,
): Pair<Int, Int> {
    val currentArea = width * height
    if (currentArea <= targetArea) {
        return width to height
    }

    val scale = sqrt(targetArea.toDouble() / currentArea)
    val newWidth = ceil(width * scale).toInt().coerceAtLeast(1)
    val newHeight = ceil(height * scale).toInt().coerceAtLeast(1)
    return newWidth to newHeight
}

internal fun ImageBitmap.extractScaledPixels(targetArea: Int): ScaledBitmapPixels {
    val (scaledWidth, scaledHeight) = calculateScaledDimensions(width, height, targetArea)

    return if (scaledWidth == width && scaledHeight == height) {
        val pixels = IntArray(width * height)
        readPixels(pixels)
        ScaledBitmapPixels(pixels, width, height)
    } else {
        val scaledBitmap = scale(scaledWidth, scaledHeight)
        val pixels = IntArray(scaledWidth * scaledHeight)
        scaledBitmap.readPixels(pixels)
        ScaledBitmapPixels(pixels, scaledWidth, scaledHeight)
    }
}
