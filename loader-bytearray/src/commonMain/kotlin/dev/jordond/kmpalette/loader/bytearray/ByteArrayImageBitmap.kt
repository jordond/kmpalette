package dev.jordond.kmpalette.loader.bytearray

import androidx.compose.ui.graphics.ImageBitmap

public expect fun ByteArray.toImageBitmap(): ImageBitmap

public fun ImageBitmap.Companion.fromByteArray(byteArray: ByteArray): ImageBitmap {
    return byteArray.toImageBitmap()
}