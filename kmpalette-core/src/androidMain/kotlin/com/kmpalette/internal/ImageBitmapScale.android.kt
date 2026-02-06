package com.kmpalette.internal

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap

/**
 * Scales this [ImageBitmap] to the specified [width] and [height] using nearest-neighbor sampling.
 *
 * Uses Android's [Bitmap.createScaledBitmap] with `filter=false` for nearest-neighbor scaling.
 */
internal actual fun ImageBitmap.scale(
    width: Int,
    height: Int,
): ImageBitmap {
    val bitmap = asAndroidBitmap()
    return Bitmap.createScaledBitmap(bitmap, width, height, false).asImageBitmap()
}
