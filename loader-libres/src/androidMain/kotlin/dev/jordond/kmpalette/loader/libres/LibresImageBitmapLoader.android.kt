package dev.jordond.kmpalette.loader.libres

import android.content.res.Resources
import android.graphics.drawable.BitmapDrawable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import io.github.skeptick.libres.images.Image

internal actual fun Image.toImageBitmap(): ImageBitmap {
    val context = ContextProvider.getInstance().context
    return loadImageBitmapResource(context.resources)
}

private fun Image.loadImageBitmapResource(res: Resources): ImageBitmap {
    try {
        return (res.getDrawable(this, null) as BitmapDrawable).bitmap.asImageBitmap()
    } catch (throwable: Throwable) {
        throw IllegalArgumentException("Unable to load resource #$this", throwable)
    }
}