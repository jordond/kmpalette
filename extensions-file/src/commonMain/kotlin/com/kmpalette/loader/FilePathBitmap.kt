package com.kmpalette.loader

import androidx.compose.ui.graphics.ImageBitmap
import okio.FileSystem
import okio.Path

internal expect val fileSystem: FileSystem

internal suspend fun Path.toImageBitmap(): ImageBitmap {
    val bytes = fileSystem.read(this) {
        readByteArray()
    }

    return ByteArrayLoader.load(bytes)
}
