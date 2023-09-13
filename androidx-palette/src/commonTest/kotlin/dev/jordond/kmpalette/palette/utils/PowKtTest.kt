package dev.jordond.kmpalette.palette.utils

import kotlin.test.Test
import kotlin.test.assertTrue

class PowKtTest {

    @Test
    fun `should return 1 when exponent is 0`() {
        assertTrue(pow(2.0, 1.0) == 2.0)
        assertTrue(pow(2.0, 2.0) == 4.0)
        assertTrue(pow(2.0, 3.0) == 8.0)
        assertTrue(pow(2.0, 4.0) == 16.0)
    }
}