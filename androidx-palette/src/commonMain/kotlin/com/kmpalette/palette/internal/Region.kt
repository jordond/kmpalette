package com.kmpalette.palette.internal

internal data class Region(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int,
) {
    val width: Int get() = right - left
    val height: Int get() = bottom - top

    fun overlaps(other: Region): Boolean =
        left < other.right && right > other.left &&
            top < other.bottom && bottom > other.top

    fun intersect(other: Region): Region =
        Region(
            left = maxOf(left, other.left),
            top = maxOf(top, other.top),
            right = minOf(right, other.right),
            bottom = minOf(bottom, other.bottom),
        )

    fun scale(factor: Float): Region =
        Region(
            left = (left * factor).toInt(),
            top = (top * factor).toInt(),
            right = kotlin.math.ceil(right * factor).toInt(),
            bottom = kotlin.math.ceil(bottom * factor).toInt(),
        )

    fun coerceIn(
        maxWidth: Int,
        maxHeight: Int,
    ): Region =
        Region(
            left = left.coerceAtLeast(0),
            top = top.coerceAtLeast(0),
            right = right.coerceAtMost(maxWidth),
            bottom = bottom.coerceAtMost(maxHeight),
        )
}
