package com.kmpalette.loader

import com.kmpalette.test.Base64Utils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
internal class AndroidBase64LoaderTest {

    @Test
    fun shouldLoadBase64ToImageBitmap() = runTest {
        val bitmap = Base64Loader.load(Base64Utils.sample1)
        assertTrue(bitmap.width > 0)
        assertTrue(bitmap.height > 0)
    }
}
