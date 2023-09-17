package com.kmpalette.loader

import com.kmpalette.loader.ByteArrayLoader
import com.kmpalette.test.ImageBitmapUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class AndroidByteArrayLoaderTest {

    @Test
    fun shouldCreateBitmap() = runTest {
        val bitmap = ByteArrayLoader.load(ImageBitmapUtils.sampleBitmapBytes())
        assertEquals(bitmap.height, ImageBitmapUtils.bitmapHeight)
        assertEquals(bitmap.width, ImageBitmapUtils.bitmapWith)
    }
}