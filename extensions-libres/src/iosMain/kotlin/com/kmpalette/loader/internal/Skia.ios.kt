@file:OptIn(ExperimentalForeignApi::class)

package com.kmpalette.loader.internal

import androidx.compose.ui.unit.IntSize
import io.github.skeptick.libres.images.Image
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.MemScope
import platform.CoreGraphics.CGImageGetHeight
import platform.CoreGraphics.CGImageGetWidth
import platform.CoreGraphics.CGImageRef
import platform.CoreGraphics.CGSizeMake
import platform.UIKit.UIGraphicsImageRenderer
import platform.UIKit.UIGraphicsImageRendererFormat

private val DefaultImageRendererFormat = UIGraphicsImageRendererFormat().apply {
    scale = 1.0
    opaque = false
}

internal actual val Image.intSize: IntSize
    get() = IntSize(
        width = CGImageGetWidth(CGImage).toInt(),
        height = CGImageGetHeight(CGImage).toInt()
    )

internal actual fun MemScope.libresImageToCGImage(image: Image, size: IntSize): CGImageRef {
    val width = size.width.toDouble()
    val height = size.height.toDouble()
    val cgSize = CGSizeMake(width, height)
    val newImage = UIGraphicsImageRenderer(cgSize, DefaultImageRendererFormat).imageWithActions {
        image.drawInRect(size.toCGRect())
    }
    return newImage.CGImage ?: error("Cannot get CGImage from UIImage")
}