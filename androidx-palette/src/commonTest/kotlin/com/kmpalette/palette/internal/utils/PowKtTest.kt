package com.kmpalette.palette.internal.utils

import com.kmpalette.palette.internal.utils.pow
import kotlin.test.Test
import kotlin.test.assertTrue

class PowKtTest {

    @Test
    fun `should calculate the exponent value`() {
        assertTrue(pow(2.0, 1.0) == 2.0)
        assertTrue(pow(2.0, 2.0) == 4.0)
        assertTrue(pow(2.0, 3.0) == 8.0)
        assertTrue(pow(2.0, 4.0) == 16.0)
    }
}