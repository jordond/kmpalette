package com.kmpalette.loader

import android.content.res.Resources
import android.content.res.Resources.Theme
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import com.kmpalette.loader.internal.ContextProvider
import io.github.skeptick.libres.images.Image

internal actual suspend fun Image.toImageBitmap(): ImageBitmap {
    val context = ContextProvider.getInstance().context

    return loadImageBitmapResource(context.resources, context.theme)
}

private fun Image.loadImageBitmapResource(resources: Resources, theme: Theme): ImageBitmap {
    try {
        return resources.getDrawable(this, theme).toBitmap().asImageBitmap()
    } catch (throwable: Throwable) {
        throw IllegalArgumentException("Unable to load resource #$this", throwable)
    }
}