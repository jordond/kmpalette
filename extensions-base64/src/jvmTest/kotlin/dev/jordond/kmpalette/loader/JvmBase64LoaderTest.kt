package dev.jordond.kmpalette.loader

import kotlinx.coroutines.test.runTest
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalEncodingApi::class)
internal class JvmBase64LoaderTest : Base64LoaderTest() {

    @Test
    fun `should load the base64 into a bitmap`() = runTest {
        val bitmap = Base64Loader.load(sample1)
        assertTrue(bitmap.width > 0)
        assertTrue(bitmap.height > 0)
    }
}
