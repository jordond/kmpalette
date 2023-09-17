package com.kmpalette.loader

import com.kmpalette.test.Base64Utils
import kotlin.test.Test
import kotlin.test.assertEquals

internal open class Base64LoaderTest {

    @Test
    fun `should strip the base64 prefix`() {
        val result = Base64Utils.sample1.stripBase64Prefix()
        assertEquals(result, Base64Utils.sample1Stripped)
    }

    @Test
    fun `should do nothing if base64 does not exist`() {
        val result = Base64Utils.sample1Stripped.stripBase64Prefix()
        assertEquals(result, Base64Utils.sample1Stripped)
    }
}

