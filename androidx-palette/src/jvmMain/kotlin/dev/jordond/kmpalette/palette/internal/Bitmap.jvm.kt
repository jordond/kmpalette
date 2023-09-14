package dev.jordond.kmpalette.palette.internal

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.graphics.toComposeImageBitmap
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.RenderingHints.KEY_RENDERING
import java.awt.RenderingHints.VALUE_RENDER_QUALITY
import java.awt.image.BufferedImage

internal actual fun ImageBitmap.scale(width: Int, height: Int): ImageBitmap {
    return toAwtImage().resize(width, height).toComposeImageBitmap()
}

private fun BufferedImage.resize(width: Int, height: Int): BufferedImage {
    val newImage = BufferedImage(width, height, BufferedImage.TRANSLUCENT)
    (newImage.createGraphics() as Graphics2D).apply {
        addRenderingHints(RenderingHints(KEY_RENDERING, VALUE_RENDER_QUALITY))
        drawImage(this@resize, 0, 0, width, height, null)
        dispose()

    }
    return newImage
}