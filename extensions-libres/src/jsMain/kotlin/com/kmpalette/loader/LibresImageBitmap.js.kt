package com.kmpalette.loader

import androidx.compose.ui.graphics.ImageBitmap
import io.github.skeptick.libres.images.Image
import kotlinx.browser.window
import kotlinx.coroutines.await
import org.khronos.webgl.Int8Array

internal actual suspend fun Image.toImageBitmap(): ImageBitmap {
    val response = window.fetch(this).await()
    if (!response.ok) {
        val message = response.statusText.ifEmpty { "Unknown error" }
        throw RuntimeException("Failed to load image: $message")
    }

    val bytes = response.arrayBuffer().await().let(::Int8Array).unsafeCast<ByteArray>()
    return ByteArrayLoader.load(bytes)
}