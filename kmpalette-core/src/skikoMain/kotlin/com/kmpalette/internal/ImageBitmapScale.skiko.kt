package com.kmpalette.internal

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image
import org.jetbrains.skia.Rect
import org.jetbrains.skia.SamplingMode
import org.jetbrains.skia.Surface

/**
 * Scales this [ImageBitmap] to the specified [width] and [height] using nearest-neighbor sampling.
 *
 * Uses Skia's [SamplingMode.DEFAULT] which is nearest-neighbor (FilterMode.NEAREST).
 */
internal actual fun ImageBitmap.scale(
    width: Int,
    height: Int,
): ImageBitmap {
    val skiaBitmap = asSkiaBitmap()
    val image = Image.makeFromBitmap(skiaBitmap)
    val surface = Surface.makeRasterN32Premul(width, height)
    val srcRect = Rect.makeWH(image.width.toFloat(), image.height.toFloat())
    val dstRect = Rect.makeWH(width.toFloat(), height.toFloat())

    surface.canvas.drawImageRect(
        image = image,
        src = srcRect,
        dst = dstRect,
        samplingMode = SamplingMode.DEFAULT,
        paint = null,
        strict = true,
    )

    return surface.makeImageSnapshot().toComposeImageBitmap()
}
