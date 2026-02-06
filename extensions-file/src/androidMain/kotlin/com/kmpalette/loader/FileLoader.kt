package com.kmpalette.loader

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.decodeToImageBitmap
import java.io.File

public object FileLoader : ImageBitmapLoader<File> {
    override suspend fun load(input: File): ImageBitmap = input.readBytes().decodeToImageBitmap()
}