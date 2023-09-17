package com.kmpalette.test

import androidx.compose.ui.graphics.ImageBitmap
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

internal expect fun loadBitmap(): ImageBitmap

object ImageBitmapUtils {

    val bitmapHeight = 24
    val bitmapWith = 24

    @OptIn(ExperimentalEncodingApi::class)
    fun sampleBitmapBytes(): ByteArray {
        return Base64.decode(Base64Utils.sample1Stripped)
    }

    fun sampleBitmap(): ImageBitmap {
        return loadBitmap()
    }
}