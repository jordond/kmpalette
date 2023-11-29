package com.kmpalette.extensions.file

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.palette.graphics.Palette
import com.kmpalette.PaletteResult
import com.kmpalette.loader.toImageBitmap
import kotlinx.coroutines.Dispatchers
import okio.Path
import okio.Path.Companion.toPath
import kotlin.coroutines.CoroutineContext

private val loader: (Path) -> suspend () -> ImageBitmap = { { it.toImageBitmap() } }

/**
 * Generate and remember a [PaletteResult] from a absolute file path [String].
 *
 * You must import the okio library to use this loader.
 *
 * @param[coroutineContext] The [CoroutineContext] to use for generating the [PaletteResult].
 * @param[block] A block to configure the [Palette.Builder].
 * @return A [PaletteResult] that will be remembered across compositions.
 */
@Composable
public fun String.rememberGeneratePalette(
    coroutineContext: CoroutineContext = Dispatchers.Default,
    block: Palette.Builder.() -> Unit = {},
): PaletteResult {
    val loader = remember(this) { loader(toPath()) }
    return com.kmpalette.rememberGeneratePalette(loader, coroutineContext, block)
}

/**
 * Generate and remember a [PaletteResult] from a [Path].
 *
 * You must import the okio library to use this loader.
 *
 * @param[coroutineContext] The [CoroutineContext] to use for generating the [PaletteResult].
 * @param[block] A block to configure the [Palette.Builder].
 * @return A [PaletteResult] that will be remembered across compositions.
 */
@Composable
public fun Path.rememberGeneratePalette(
    coroutineContext: CoroutineContext = Dispatchers.Default,
    block: Palette.Builder.() -> Unit = {},
): PaletteResult {
    val loader = remember(this) { loader(this) }
    return com.kmpalette.rememberGeneratePalette(loader, coroutineContext, block)
}