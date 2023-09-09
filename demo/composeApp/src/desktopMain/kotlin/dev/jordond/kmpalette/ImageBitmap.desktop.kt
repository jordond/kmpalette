@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalComposeUiApi::class)

package dev.jordond.kmpalette

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.ResourceLoader
import androidx.compose.ui.res.loadImageBitmap
import io.github.skeptick.libres.images.Image
import java.io.InputStream

@Composable
actual fun Image.toImageBitmap(): ImageBitmap {
    return remember(this) {
        useResource(this, ::loadImageBitmap)
    }
}

private inline fun <T> useResource(
    resourcePath: String,
    block: (InputStream) -> T,
): T = ResourceLoader.Default.load(resourcePath).use(block)