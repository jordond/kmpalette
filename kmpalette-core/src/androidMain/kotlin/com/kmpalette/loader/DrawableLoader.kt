package com.kmpalette.loader

import android.content.Context
import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext

/**
 * A loader for Android drawable resources that converts them to [ImageBitmap] for palette generation.
 *
 * This loader uses the Android [Context] to decode drawable resource IDs into bitmaps.
 *
 * @param[context] The Android [Context] used for loading drawable resources.
 */
public class DrawableLoader(
    private val context: Context,
) : ImageBitmapLoader<Int> {
    override suspend fun load(
        @DrawableRes input: Int,
    ): ImageBitmap {
        val bitmap = BitmapFactory.decodeResource(context.resources, input)
        return bitmap.asImageBitmap()
    }
}

/**
 * Create and remember a [DrawableLoader] that uses the [LocalContext] to load drawables.
 *
 * @param[context] The [Context] to use for loading drawables.
 * @return A [DrawableLoader] for the current [LocalContext].
 */
@Composable
public fun rememberDrawableLoader(context: Context = LocalContext.current): DrawableLoader =
    remember(context) { DrawableLoader(context) }
