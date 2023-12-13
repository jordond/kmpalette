package com.kmpalette.internal

import android.graphics.Bitmap
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import android.view.SurfaceView
import android.view.ViewGroup
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlinx.coroutines.isActive
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

internal actual suspend fun createBitmapFrom(
    painter: Painter,
    density: Density,
    layoutDirection: LayoutDirection,
    width: Int,
    height: Int,
): ImageBitmap {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
        return defaultCreateBitmapFrom(painter, density, layoutDirection, width, height)
    } else {
        val context = ContextProvider.getInstance().context
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val surfaceView = SurfaceView(context).apply {
            layoutParams = ViewGroup.LayoutParams(width, height)
        }

        return suspendCancellableCoroutine<ImageBitmap> { continuation ->
            PixelCopy.request(
                surfaceView,
                bitmap,
                { copyResult ->
                    val result =
                        if (copyResult == PixelCopy.SUCCESS) bitmap.asImageBitmap()
                        else defaultCreateBitmapFrom(painter, density, layoutDirection, width, height)

                    if (continuation.context.isActive) {
                        continuation.resume(result)
                    }
                },
                Handler(Looper.getMainLooper())
            )
        }
    }
}