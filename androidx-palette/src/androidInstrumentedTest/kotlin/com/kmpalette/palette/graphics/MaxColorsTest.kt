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

package com.kmpalette.palette.graphics

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.kmpalette.palette.graphics.TestUtils.loadSampleBitmap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MaxColorsTest {

    @Test
    @SmallTest
    fun testMaxColorCount32() {
        testMaxColorCount(32)
    }

    @Test
    @SmallTest
    fun testMaxColorCount1() {
        testMaxColorCount(1)
    }

    @Test
    @SmallTest
    fun testMaxColorCount15() {
        testMaxColorCount(15)
    }

    private fun testMaxColorCount(colorCount: Int) = runTest {
        val newPalette = Palette.from(loadSampleBitmap())
            .maximumColorCount(colorCount)
            .generate()
        Assert.assertTrue(newPalette.swatches.size <= colorCount)
    }
}