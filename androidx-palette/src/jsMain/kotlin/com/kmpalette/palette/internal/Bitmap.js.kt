package com.kmpalette.palette.internal

import androidx.compose.ui.graphics.ImageBitmap

internal actual fun ImageBitmap.scale(width: Int, height: Int): ImageBitmap {
    return nativeScale(width, height)
}