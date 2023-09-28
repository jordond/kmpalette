package com.kmpalette.loader

import com.kmpalette.test.Base64Utils
import kotlin.test.Test
import kotlin.test.assertEquals

internal open class Base64LoaderTest {

    @Test
    fun should_strip_the_base64_prefix() {
        val result = Base64Utils.sample1.stripBase64Prefix()
        assertEquals(result, Base64Utils.sample1Stripped)
    }

    @Test
    fun should_do_nothing_if_base64_does_not_exist() {
        val result = Base64Utils.sample1Stripped.stripBase64Prefix()
        assertEquals(result, Base64Utils.sample1Stripped)
    }
}

