package dev.jordond.kmpalette

import androidx.annotation.CheckResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import dev.jordond.kmpalette.loader.toImageBitmap
import dev.jordond.kmpalette.palette.graphics.Palette
import io.github.skeptick.libres.images.Image
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

private val loader: (Image) -> suspend () -> ImageBitmap = { { it.toImageBitmap() } }

/**
 * Generate and remember a [PaletteResult] from a [Image].
 *
 * @param[coroutineContext] The [CoroutineContext] to use for generating the [PaletteResult].
 * @param[block] A block to configure the [Palette.Builder].
 * @return A [PaletteResult] that will be remembered across compositions.
 */
@Composable
@CheckResult
public fun Image.rememberGeneratePalette(
    coroutineContext: CoroutineContext = Dispatchers.Default,
    block: Palette.Builder.() -> Unit = {},
): PaletteResult {
    val loader = remember(this) { loader(this) }
    return rememberGeneratePalette(loader, coroutineContext, block)
}