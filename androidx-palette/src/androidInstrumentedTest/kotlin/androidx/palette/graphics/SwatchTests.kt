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

@file:OptIn(ExperimentalCoroutinesApi::class)

package androidx.palette.graphics

import android.graphics.Color
import androidx.core.graphics.ColorUtils
import androidx.palette.graphics.Palette.Swatch
import androidx.palette.graphics.TestUtils.loadSampleBitmap
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SwatchTests {

    @Test
    @SmallTest
    fun testTextColorContrasts() = runTest {
        val p = Palette.from(loadSampleBitmap()).generate()
        for (swatch in p.swatches) {
            testSwatchTextColorContrasts(swatch)
        }
    }

    @Test
    @SmallTest
    fun testHslNotNull() = runTest {
        val p = Palette.from(loadSampleBitmap()).generate()
        for (swatch in p.swatches) {
            Assert.assertNotNull(swatch.hsl)
        }
    }

    @Test
    @SmallTest
    fun testHslIsRgb() = runTest {
        val p = Palette.from(loadSampleBitmap()).generate()
        for (swatch in p.swatches) {
            Assert.assertEquals(ColorUtils.HSLToColor(swatch.hsl).toLong(), swatch.rgb.toLong())
        }
    }

    private fun testSwatchTextColorContrasts(swatch: Swatch) {
        val bodyTextColor = swatch.bodyTextColor
        Assert.assertTrue(ColorUtils.calculateContrast(bodyTextColor, swatch.rgb) >= MIN_CONTRAST_BODY_TEXT)
        val titleTextColor = swatch.titleTextColor
        Assert.assertTrue(ColorUtils.calculateContrast(titleTextColor, swatch.rgb) >= MIN_CONTRAST_TITLE_TEXT)
    }

    @Test
    @SmallTest
    fun testEqualsWhenSame() {
        val swatch1 = Swatch(Color.WHITE, 50)
        val swatch2 = Swatch(Color.WHITE, 50)
        Assert.assertEquals(swatch1, swatch2)
    }

    @Test
    @SmallTest
    fun testEqualsWhenColorDifferent() {
        val swatch1 = Swatch(Color.BLACK, 50)
        val swatch2 = Swatch(Color.WHITE, 50)
        Assert.assertFalse(swatch1 == swatch2)
    }

    @Test
    @SmallTest
    fun testEqualsWhenPopulationDifferent() {
        val swatch1 = Swatch(Color.BLACK, 50)
        val swatch2 = Swatch(Color.BLACK, 100)
        Assert.assertFalse(swatch1 == swatch2)
    }

    @Test
    @SmallTest
    fun testEqualsWhenDifferent() {
        val swatch1 = Swatch(Color.BLUE, 50)
        val swatch2 = Swatch(Color.BLACK, 100)
        Assert.assertFalse(swatch1 == swatch2)
    }

    companion object {

        private const val MIN_CONTRAST_TITLE_TEXT = 3.0f
        private const val MIN_CONTRAST_BODY_TEXT = 4.5f
    }
}

