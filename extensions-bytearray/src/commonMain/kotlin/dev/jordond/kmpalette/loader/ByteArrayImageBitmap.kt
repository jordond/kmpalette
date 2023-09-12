package dev.jordond.kmpalette.loader

import androidx.compose.ui.graphics.ImageBitmap

internal expect fun ByteArray.toImageBitmap(): ImageBitmap
