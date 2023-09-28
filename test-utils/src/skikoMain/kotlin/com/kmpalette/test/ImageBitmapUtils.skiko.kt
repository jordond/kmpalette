package com.kmpalette.test

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import org.jetbrains.skia.Bitmap

internal actual fun loadBitmap(): ImageBitmap {
    val bitmap = Bitmap()
    bitmap.installPixels(ImageBitmapUtils.sampleBitmapBytes())
    return bitmap.asComposeImageBitmap()
}