package com.kmpalette.palette.graphics

import android.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import com.kmpalette.test.ImageBitmapUtils
import kotlin.test.assertEquals

internal object TestUtils {
    fun loadSampleBitmap(): ImageBitmap = ImageBitmapUtils.sampleBitmap()

    fun assertCloseColors(
        expected: Int,
        actual: Int,
    ) {
        assertEquals(Color.red(expected), Color.red(actual), 8)
        assertEquals(Color.green(expected), Color.green(actual), 8)
        assertEquals(Color.blue(expected), Color.blue(actual), 8)
    }

    fun assertEquals(
        expected: Int,
        actual: Int,
        absoluteTolerance: Int,
        message: String? = null,
    ) {
        assertEquals(expected.toDouble(), actual.toDouble(), absoluteTolerance.toDouble(), message)
    }
}
