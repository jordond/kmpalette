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

import androidx.compose.ui.graphics.ImageBitmap
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ConsistencyTest {

    @Test
    @SmallTest
    fun testConsistency() = runTest {
        var lastPalette: Palette? = null
        val bitmap: ImageBitmap = TestUtils.loadSampleBitmap()
        for (i in 0 until NUMBER_TRIALS) {
            val newPalette: Palette = Palette.from(bitmap).generate()
            if (lastPalette != null) {
                assetPalettesEqual(lastPalette, newPalette)
            }
            lastPalette = newPalette
        }
    }

    companion object {

        private const val NUMBER_TRIALS = 10

        private fun assetPalettesEqual(p1: Palette, p2: Palette) {
            Assert.assertEquals(p1.vibrantSwatch, p2.vibrantSwatch)
            Assert.assertEquals(p1.lightVibrantSwatch, p2.lightVibrantSwatch)
            Assert.assertEquals(p1.darkVibrantSwatch, p2.darkVibrantSwatch)
            Assert.assertEquals(p1.mutedSwatch, p2.mutedSwatch)
            Assert.assertEquals(p1.lightMutedSwatch, p2.lightMutedSwatch)
            Assert.assertEquals(p1.darkMutedSwatch, p2.darkMutedSwatch)
        }
    }
}
