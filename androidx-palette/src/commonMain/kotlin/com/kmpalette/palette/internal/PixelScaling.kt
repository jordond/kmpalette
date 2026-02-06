package com.kmpalette.palette.internal

import kotlin.math.ceil
import kotlin.math.sqrt

internal fun scalePixelsNearestNeighbor(
    pixels: IntArray,
    srcWidth: Int,
    srcHeight: Int,
    dstWidth: Int,
    dstHeight: Int,
): IntArray {
    if (dstWidth == srcWidth && dstHeight == srcHeight) {
        return pixels
    }

    val result = IntArray(dstWidth * dstHeight)
    val xRatio = srcWidth.toFloat() / dstWidth
    val yRatio = srcHeight.toFloat() / dstHeight

    for (y in 0 until dstHeight) {
        val srcY = (y * yRatio).toInt().coerceIn(0, srcHeight - 1)
        val srcRowOffset = srcY * srcWidth
        val dstRowOffset = y * dstWidth

        for (x in 0 until dstWidth) {
            val srcX = (x * xRatio).toInt().coerceIn(0, srcWidth - 1)
            result[dstRowOffset + x] = pixels[srcRowOffset + srcX]
        }
    }
    return result
}

internal data class ScaledPixels(
    val pixels: IntArray,
    val width: Int,
    val height: Int,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as ScaledPixels
        return width == other.width && height == other.height && pixels.contentEquals(other.pixels)
    }

    override fun hashCode(): Int {
        var result = pixels.contentHashCode()
        result = 31 * result + width
        result = 31 * result + height
        return result
    }
}

internal fun scalePixelsToArea(
    pixels: IntArray,
    width: Int,
    height: Int,
    maxArea: Int,
): ScaledPixels {
    val currentArea = width * height
    if (currentArea <= maxArea || maxArea <= 0) {
        return ScaledPixels(pixels, width, height)
    }

    val scale = sqrt(maxArea.toDouble() / currentArea)
    val newWidth = ceil(width * scale).toInt().coerceAtLeast(1)
    val newHeight = ceil(height * scale).toInt().coerceAtLeast(1)

    val scaledPixels =
        scalePixelsNearestNeighbor(
            pixels = pixels,
            srcWidth = width,
            srcHeight = height,
            dstWidth = newWidth,
            dstHeight = newHeight,
        )
    return ScaledPixels(scaledPixels, newWidth, newHeight)
}
