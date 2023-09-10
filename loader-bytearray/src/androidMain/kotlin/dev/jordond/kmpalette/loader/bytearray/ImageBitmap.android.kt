package dev.jordond.kmpalette.loader.bytearray

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

public actual fun ByteArray.toImageBitmap(): ImageBitmap {
    return toAndroidBitmap().asImageBitmap()
}

private fun ByteArray.toAndroidBitmap(): Bitmap {
    return BitmapFactory.decodeByteArray(this, 0, size);
}