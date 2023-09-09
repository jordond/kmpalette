package dev.jordond.kmpalette

import android.content.res.Resources
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import io.github.skeptick.libres.images.Image

@Composable
actual fun Image.toImageBitmap(): ImageBitmap {
    val context = LocalContext.current
    val res = resources()
    return remember(this, res, context.theme) {
        loadImageBitmapResource(res)
    }
}

private fun Image.loadImageBitmapResource(res: Resources): ImageBitmap {
    try {
        return ImageBitmap.imageResource(res, this)
    } catch (throwable: Throwable) {
        throw IllegalArgumentException("Unable to load resource #$this", throwable)
    }
}

/**
 * A composable function that returns the [Resources]. It will be recomposed when [Configuration]
 * gets updated.
 */
@Composable
@ReadOnlyComposable
internal fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}