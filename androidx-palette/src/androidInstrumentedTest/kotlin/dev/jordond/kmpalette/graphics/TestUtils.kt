package dev.jordond.kmpalette.graphics

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.test.core.app.ApplicationProvider
import dev.jordond.kmpalette.palette.test.R
import kotlin.test.assertEquals

internal object TestUtils {

    fun loadSampleBitmap(): ImageBitmap {
        return BitmapFactory.decodeResource(
            ApplicationProvider.getApplicationContext<Context>().resources,
            R.drawable.photo
        ).asImageBitmap()
    }

    fun assertCloseColors(expected: Int, actual: Int) {
        assertEquals(Color.red(expected), Color.red(actual), 8)
        assertEquals(Color.green(expected), Color.green(actual), 8)
        assertEquals(Color.blue(expected), Color.blue(actual), 8)
    }

    fun assertEquals(expected: Int, actual: Int, absoluteTolerance: Int, message: String? = null) {
        assertEquals(expected.toDouble(), actual.toDouble(), absoluteTolerance.toDouble(), message)
    }
}
