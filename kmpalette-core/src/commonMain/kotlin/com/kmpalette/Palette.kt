package com.kmpalette

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import com.kmpalette.palette.graphics.Palette
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/**
 * Generates a [Palette] from this [ImageBitmap].
 *
 * @param[coroutineContext] The [CoroutineContext] to use for the generation of the [Palette].
 * @param[block] A block to configure the [Palette.Builder] before generating the [Palette].
 * @return The generated [Palette].
 */
public suspend fun ImageBitmap.generatePalette(
    coroutineContext: CoroutineContext = Dispatchers.Default,
    block: Palette.Builder.() -> Unit = {},
): Palette {
    return withContext(coroutineContext) {
        Palette.from(bitmap = this@generatePalette).apply(block).generate()
    }
}

/**
 * Generates a [Palette] from this [ImageBitmap] and remembers the result.
 *
 * @param[coroutineContext] The [CoroutineContext] to use for the generation of the [Palette].
 * @param[block] A block to configure the [Palette.Builder] before generating the [Palette].
 * @return The generated [Palette].
 */
@Composable
public fun ImageBitmap.rememberGeneratePalette(
    coroutineContext: CoroutineContext = Dispatchers.Default,
    block: Palette.Builder.() -> Unit = {},
): PaletteResult {
    var palette: PaletteResult by remember(this) { mutableStateOf(PaletteResult.Loading) }

    LaunchedEffect(this, coroutineContext, block) {
        palette = try {
            val result = generatePalette(coroutineContext, block)
            PaletteResult.Success(result)
        } catch (cause: Throwable) {
            if (cause is CancellationException) throw cause
            PaletteResult.Error(cause)
        }
    }

    return palette
}

/**
 * Generates a [Palette] from this [ImageBitmap] and remembers the result.
 *
 * @param[loader] Loader to load the [ImageBitmap] from.
 * @param[coroutineContext] The [CoroutineContext] to use for the generation of the [Palette].
 * @param[block] A block to configure the [Palette.Builder] before generating the [Palette].
 * @return The generated [Palette].
 */
@Composable
public fun rememberGeneratePalette(
    loader: suspend () -> ImageBitmap,
    coroutineContext: CoroutineContext = Dispatchers.Default,
    block: Palette.Builder.() -> Unit = {},
): PaletteResult {
    var result: PaletteResult by remember(loader) { mutableStateOf(PaletteResult.Loading) }
    var bitmap: ImageBitmap? by remember(loader) { mutableStateOf(null) }

    LaunchedEffect(loader) {
        try {
            bitmap = loader()
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

/**
 * Wrapper result class for the generation of a [Palette].
 */
public sealed interface PaletteResult {

    /**
     * A successful result of a [Palette] generation.
     *
     * @property[palette] The generated [Palette].
     */
    public data class Success(val palette: Palette) : PaletteResult

    /**
     * Palette is being generated.
     */
    public data object Loading : PaletteResult

    /**
     * An error occurred while generating the [Palette].
     *
     * @property[cause] The cause of the error.
     */
    public data class Error(val cause: Throwable) : PaletteResult

    /**
     * Returns the generated [Palette] if this is a [Success] result, otherwise `null`.
     */
    public val paletteOrNull: Palette?
        get() = when (this) {
            is Success -> palette
            else -> null
        }
}