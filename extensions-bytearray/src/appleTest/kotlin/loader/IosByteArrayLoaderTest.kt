package loader

import dev.jordond.kmpalette.loader.ByteArrayLoader
import dev.jordond.kmpalette.test.ImageBitmapUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class IosByteArrayLoaderTest {

    @Test
    fun `should create a bitmap`() = runTest {
        val bitmap = ByteArrayLoader.load(ImageBitmapUtils.sampleBitmapBytes())
        assertEquals(bitmap.height, ImageBitmapUtils.bitmapHeight)
        assertEquals(bitmap.width, ImageBitmapUtils.bitmapWith)
    }
}