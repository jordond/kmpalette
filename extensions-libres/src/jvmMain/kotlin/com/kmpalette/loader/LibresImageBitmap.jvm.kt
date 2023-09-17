@file:OptIn(ExperimentalComposeUiApi::class)

package com.kmpalette.loader

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.ResourceLoader
import androidx.compose.ui.res.loadImageBitmap
import io.github.skeptick.libres.images.Image
import java.io.InputStream

internal actual suspend fun Image.toImageBitmap(): ImageBitmap {
    return useResource(this, ::loadImageBitmap)
}

private inline fun <T> useResource(
    resourcePath: String,
    block: (InputStream) -> T,
): T = ResourceLoader.Default.load(resourcePath).use(block)