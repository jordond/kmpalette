package dev.jordond.kmpalette.palette.internal

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import korlibs.image.bitmap.Bitmap
import korlibs.image.bitmap.Bitmap32
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ColorInfo
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.ImageInfo

internal expect fun ImageBitmap.scale(width: Int, height: Int): ImageBitmap

internal fun Bitmap.toComposeImageBitmap(): ImageBitmap {
    return toBMP32IfRequired().toSkiaBitmap().asComposeImageBitmap().also {
        it.asSkiaBitmap()
    }
}

internal fun Bitmap32.toSkiaBitmap(): org.jetbrains.skia.Bitmap {
    val colorType = ColorType.N32
    val alphaType = if (premultiplied) ColorAlphaType.PREMUL else ColorAlphaType.OPAQUE
    val skiaColorSpace = null
    val colorInfo = ColorInfo(colorType, alphaType, skiaColorSpace)
    val imageInfo = ImageInfo(colorInfo, width, height)
    val bitmap = org.jetbrains.skia.Bitmap()
    val pixels = extractBytes()
    bitmap.allocPixels(imageInfo)
    bitmap.installPixels(pixels)
    return bitmap
}