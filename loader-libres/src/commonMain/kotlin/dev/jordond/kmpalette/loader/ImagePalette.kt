package dev.jordond.kmpalette.loader

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import dev.jordond.kmpalette.palette.graphics.Palette
import dev.jordond.kmpalette.rememberGeneratePalette
import io.github.skeptick.libres.images.Image
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

@Composable
public fun Image?.rememberGeneratePalette(
    coroutineContext: CoroutineContext = Dispatchers.Default,
    block: Palette.Builder.() -> Unit = {},
): Palette? {
    val bitmap: ImageBitmap? = remember(this) { this?.toImageBitmap() }
    return bitmap?.rememberGeneratePalette(coroutineContext, block)
}