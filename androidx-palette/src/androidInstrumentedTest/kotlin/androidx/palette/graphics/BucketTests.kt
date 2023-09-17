/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package androidx.palette.graphics

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.compose.ui.graphics.asImageBitmap
import androidx.palette.graphics.TestUtils.assertCloseColors
import androidx.palette.graphics.TestUtils.loadSampleBitmap
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class BucketTests {

    @Test
    @SmallTest
    fun testSwatchesBuilder() {
        val swatches = ArrayList<Palette.Swatch>()
        swatches.add(Palette.Swatch(Color.BLACK, 40))
        swatches.add(Palette.Swatch(Color.GREEN, 60))
        swatches.add(Palette.Swatch(Color.BLUE, 10))
        val p = Palette.from(swatches)
        Assert.assertEquals(swatches, p.swatches)
    }

    @Test
    @SmallTest
    fun testRegionWhole() = runTest {
        val sample = loadSampleBitmap()
        val b: Palette.Builder = Palette.Builder(sample)
        b.setRegion(0, 0, sample.width, sample.height)
        b.generate()
    }

    @Test
    @SmallTest
    fun testRegionUpperLeft() = runTest {
        val sample = loadSampleBitmap()
        val b: Palette.Builder = Palette.Builder(sample)
        b.setRegion(0, 0, sample.width / 2, sample.height / 2)
        b.generate()
    }

    @Test
    @SmallTest
    fun testRegionBottomRight() = runTest {
        val sample = loadSampleBitmap()
        val b: Palette.Builder = Palette.Builder(sample)
        b.setRegion(sample.width / 2, sample.height / 2,
            sample.width, sample.height)
        b.generate()
    }

    @Test
    @SmallTest
    fun testOnePixelTallBitmap() = runTest {
        val bitmap = Bitmap.createBitmap(1000, 1, Bitmap.Config.ARGB_8888).asImageBitmap()
        val b: Palette.Builder = Palette.Builder(bitmap)
        b.generate()
    }

    @Test
    @SmallTest
    fun testOnePixelWideBitmap() {
        val bitmap = Bitmap.createBitmap(1, 1000, Bitmap.Config.ARGB_8888).asImageBitmap()
        val b: Palette.Builder = Palette.Builder(bitmap)
        b.generate()
    }

    @Test
    @SmallTest
    fun testBlueBitmapReturnsBlueSwatch() {
        val bitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.BLUE)
        val palette: Palette = Palette.from(bitmap.asImageBitmap()).generate()
        Assert.assertEquals(1, palette.swatches.size.toLong())
        val swatch = palette.swatches[0]
        assertCloseColors(Color.BLUE, swatch.rgb)
    }

    @Test
    @SmallTest
    fun testBlueBitmapWithRegionReturnsBlueSwatch() {
        val bitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.BLUE)
        val palette: Palette = Palette.from(bitmap.asImageBitmap())
            .setRegion(0, bitmap.height / 2, bitmap.width, bitmap.height)
            .generate()
        Assert.assertEquals(1, palette.swatches.size.toLong())
        val swatch = palette.swatches[0]
        assertCloseColors(Color.BLUE, swatch.rgb)
    }

    @Test
    @SmallTest
    fun testDominantSwatch() {
        val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)

        // First fill the canvas with blue
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.BLUE)
        val paint = Paint()
        // Now we'll draw the top 10px tall rect with green
        paint.color = Color.GREEN
        canvas.drawRect(0f, 0f, 100f, 10f, paint)

        // Now we'll draw the next 20px tall rect with red
        paint.color = Color.RED
        canvas.drawRect(0f, 11f, 100f, 30f, paint)

        // Now generate a palette from the bitmap
        val palette: Palette = Palette.from(bitmap.asImageBitmap()).generate()

        // First assert that there are 3 swatches
        Assert.assertEquals(3, palette.swatches.size.toLong())

        // Now assert that the dominant swatch is blue
        val swatch = palette.dominantSwatch
        Assert.assertNotNull(swatch)
        assertCloseColors(Color.BLUE, swatch!!.rgb)
    }
}
