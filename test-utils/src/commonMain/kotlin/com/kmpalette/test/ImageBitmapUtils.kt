package com.kmpalette.test

import androidx.compose.ui.graphics.ImageBitmap
import kotlin.io.encoding.Base64

internal expect fun loadBitmap(): ImageBitmap

public object ImageBitmapUtils {

    public val bitmapHeight: Int = 24
    public val bitmapWidth: Int = 24

    public fun sampleBitmapBytes(): ByteArray {
        return Base64.decode(Base64Utils.sample1Stripped)
    }

    public fun sampleBitmap(): ImageBitmap {
        return loadBitmap()
    }
}