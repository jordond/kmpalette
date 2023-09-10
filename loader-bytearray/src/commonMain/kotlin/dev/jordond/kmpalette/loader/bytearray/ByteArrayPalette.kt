package dev.jordond.kmpalette.loader.bytearray

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import dev.jordond.kmpalette.palette.graphics.Palette
import dev.jordond.kmpalette.rememberGeneratePalette
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

@Composable
public fun ByteArray.rememberGeneratePalette(
    coroutineContext: CoroutineContext = Dispatchers.Default,
    block: Palette.Builder.() -> Unit = {},
): Palette? {
    val bitmap: ImageBitmap = remember(this) { toImageBitmap() }
    return bitmap.rememberGeneratePalette(coroutineContext, block)
}