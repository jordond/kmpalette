package com.kmpalette.internal

import androidx.compose.ui.graphics.ImageBitmap

/**
 * Scales this [ImageBitmap] to the specified [width] and [height] using nearest-neighbor sampling.
 *
 * This is used to reduce memory consumption when extracting a color palette from large images.
 * Nearest-neighbor sampling preserves vibrant colors better than bilinear interpolation.
 */
internal expect fun ImageBitmap.scale(
    width: Int,
    height: Int,
): ImageBitmap
