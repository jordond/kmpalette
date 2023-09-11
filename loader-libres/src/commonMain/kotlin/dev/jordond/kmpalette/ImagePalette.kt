package dev.jordond.kmpalette

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import dev.jordond.kmpalette.loader.toImageBitmap
import dev.jordond.kmpalette.palette.graphics.Palette
import io.github.skeptick.libres.images.Image
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

@Composable
public fun Image.rememberGeneratePalette(
    coroutineContext: CoroutineContext = Dispatchers.Default,
    block: Palette.Builder.() -> Unit = {},
): PaletteResult {
    var result: PaletteResult by remember(this) { mutableStateOf(PaletteResult.Loading) }
    var bitmap: ImageBitmap? by remember(this) { mutableStateOf(null) }
    LaunchedEffect(this) {
        try {
            bitmap = toImageBitmap()
        } catch (cause: Throwable) {
            if (cause is CancellationException) throw cause
            result = PaletteResult.Error(cause)
        }
    }

    bitmap?.let { imageBitmap ->
        result = imageBitmap.rememberGeneratePalette(coroutineContext, block)
    }

    return result
}