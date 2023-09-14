package dev.jordond.kmpalette.loader

import dev.jordond.kmpalette.test.Base64Utils
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue

internal class JvmBase64LoaderTest : Base64LoaderTest() {

    @Test
    fun `should load the base64 into a bitmap`() = runTest {
        val bitmap = Base64Loader.load(Base64Utils.sample1)
        assertTrue(bitmap.width > 0)
        assertTrue(bitmap.height > 0)
    }
}
