package com.kmpalette.extensions.bytearray

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import com.kmpalette.PaletteResult
import com.kmpalette.loader.toImageBitmap
import com.kmpalette.palette.graphics.Palette
import kotlin.coroutines.CoroutineContext

private val loader: (ByteArray) -> suspend () -> ImageBitmap = { { it.toImageBitmap() } }

/**
 * Generate and remember a [PaletteResult] from a [ByteArray].
 *
 * @param[coroutineContext] The [CoroutineContext] to use for generating the [PaletteResult].
 * @param[block] A block to configure the [Palette.Builder].
 * @return A [PaletteResult] that will be remembered across compositions.
 */
@Composable
public fun ByteArray.rememberGeneratePalette(
    coroutineContext: CoroutineContext = kotlinx.coroutines.Dispatchers.Default,
    block: Palette.Builder.() -> Unit = {},
): PaletteResult {
    val loader = remember(this) { loader(this) }
    return com.kmpalette.rememberGeneratePalette(loader, coroutineContext, block)
}