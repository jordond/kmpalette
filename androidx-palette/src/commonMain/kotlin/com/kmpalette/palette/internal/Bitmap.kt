package com.kmpalette.palette.internal

import androidx.compose.ui.graphics.ImageBitmap

internal expect fun ImageBitmap.scale(width: Int, height: Int): ImageBitmap
