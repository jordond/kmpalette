package com.kmpalette.extensions.resources

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import com.kmpalette.PaletteResult
import com.kmpalette.loader.toImageBitmap
import com.kmpalette.palette.graphics.Palette
import kotlinx.coroutines.Dispatchers
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Resource
import kotlin.coroutines.CoroutineContext

@ExperimentalResourceApi
private val loader: (Resource) -> suspend () -> ImageBitmap = { { it.toImageBitmap() } }

/**
 * Generate and remember a [PaletteResult] from a [Resource].
 *
 * @param[coroutineContext] The [CoroutineContext] to use for generating the [PaletteResult].
 * @param[block] A block to configure the [Palette.Builder].
 * @return A [PaletteResult] that will be remembered across compositions.
 */
@Composable
@ExperimentalResourceApi
public fun Resource.rememberGeneratePalette(
    coroutineContext: CoroutineContext = Dispatchers.Default,
    block: Palette.Builder.() -> Unit = {},
): PaletteResult {
    val loader = remember(this) { loader(this) }
    return com.kmpalette.rememberGeneratePalette(loader, coroutineContext, block)
}