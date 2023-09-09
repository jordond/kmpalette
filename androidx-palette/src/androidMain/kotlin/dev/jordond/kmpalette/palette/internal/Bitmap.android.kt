package dev.jordond.kmpalette.palette.internal

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap

internal actual fun ImageBitmap.scale(width: Int, height: Int): ImageBitmap {
    val bitmap = asAndroidBitmap()
    Bitmap.createScaledBitmap(bitmap, width, height, false)
    return bitmap.asImageBitmap()
}