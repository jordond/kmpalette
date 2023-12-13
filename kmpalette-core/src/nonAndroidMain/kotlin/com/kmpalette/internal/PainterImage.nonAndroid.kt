package com.kmpalette.internal

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

internal actual suspend fun createBitmapFrom(
    painter: Painter,
    density: Density,
    layoutDirection: LayoutDirection,
    width: Int,
    height: Int,
): ImageBitmap = defaultCreateBitmapFrom(painter, density, layoutDirection, width, height)