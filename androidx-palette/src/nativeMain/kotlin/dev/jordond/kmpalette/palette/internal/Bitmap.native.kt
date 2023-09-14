package dev.jordond.kmpalette.palette.internal

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.graphics.toPixelMap
import korlibs.image.bitmap.NativeImage
import korlibs.image.bitmap.resized
import korlibs.math.geom.Anchor
import korlibs.math.geom.ScaleMode
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ImageInfo
import org.jetbrains.skia.Bitmap as SkiaBitmap

internal actual fun ImageBitmap.scale(width: Int, height: Int): ImageBitmap {
    val pixels: IntArray = this.toPixelMap().buffer
    val scaled = NativeImage(width, height, premultiplied = false)
    scaled.writePixelsUnsafe(x = 0, y = 0, width, height, pixels, offset = 0)
    scaled.resized(width, height, ScaleMode.COVER, Anchor.CENTER, native = true)

    val scaledPixels = scaled.readPixelsUnsafe(0, 0, scaled.width, scaled.height)
    return imageBitmapFromArgb(scaledPixels, width, height)
}

private fun imageBitmapFromArgb(rawArgbImageData: IntArray, width: Int, height: Int): ImageBitmap {
    val bytesPerPixel = 4
    val pixels = ByteArray(width * height * bytesPerPixel)

    var k = 0
    for (y in 0 until height) {
        for (x in 0 until width) {
            val argb = rawArgbImageData[y * width + x]
            val a = (argb shr 24) and 0xff
            val r = (argb shr 16) and 0xff
            val g = (argb shr 8) and 0xff
            val b = (argb shr 0) and 0xff
            pixels[k++] = b.toByte()
            pixels[k++] = g.toByte()
            pixels[k++] = r.toByte()
            pixels[k++] = a.toByte()
        }
    }

    return imageBitmapFromArgb(pixels, width, height)
}

private fun imageBitmapFromArgb(rawArgbImageData: ByteArray, width: Int, height: Int): ImageBitmap {
    val bitmap = SkiaBitmap()
    bitmap.allocPixels(ImageInfo.makeS32(width, height, ColorAlphaType.UNPREMUL))
    bitmap.installPixels(rawArgbImageData)
    return bitmap.asComposeImageBitmap()
}