package com.kmpalette

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import com.kmpalette.internal.extractScaledPixels
import com.kmpalette.palette.graphics.Palette
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/**
 * Creates a [Palette.Builder] from an [ImageBitmap].
 *
 * This extension function extracts and scales the pixels from the bitmap, then creates
 * a builder configured with the appropriate region coordinate space.
 *
 * @param[bitmap] The [ImageBitmap] to extract colors from.
 * @return A [Palette.Builder] configured for the given bitmap.
 */
public fun Palette.Companion.from(bitmap: ImageBitmap): Palette.Builder {
    val scaled = bitmap.extractScaledPixels(Palette.DEFAULT_RESIZE_BITMAP_AREA)
    return Palette
        .from(scaled.pixels, scaled.width, scaled.height)
        .scaling(false)
        .setRegionCoordinateSpace(bitmap.width, bitmap.height)
}

/**
 * Generates a [Palette] from this [ImageBitmap].
 *
 * This suspend function extracts colors from the bitmap and generates a palette
 * asynchronously on the specified coroutine context.
 *
 * @param[coroutineContext] The [CoroutineContext] to use for palette generation. Defaults to [Dispatchers.Default].
 * @param[block] Optional lambda to configure the [Palette.Builder] before generation.
 * @return The generated [Palette] containing color swatches extracted from the bitmap.
 */
public suspend fun ImageBitmap.generatePalette(
    coroutineContext: CoroutineContext = Dispatchers.Default,
    block: Palette.Builder.() -> Unit = {},
): Palette =
    withContext(coroutineContext) {
        Palette.from(bitmap = this@generatePalette).apply(block).generate()
    }

/**
 * Generates and remembers a [Palette] from this [ImageBitmap] across compositions.
 *
 * The palette generation is triggered in a [LaunchedEffect] and the result is
 * remembered across recompositions. The result starts as [PaletteResult.Loading]
 * and transitions to either [PaletteResult.Success] or [PaletteResult.Error].
 *
 * @param[coroutineContext] The [CoroutineContext] to use for palette generation. Defaults to [Dispatchers.Default].
 * @param[block] Optional lambda to configure the [Palette.Builder] before generation.
 * @return A [PaletteResult] representing the current state of palette generation.
 */
@Composable
public fun ImageBitmap.rememberGeneratePalette(
    coroutineContext: CoroutineContext = Dispatchers.Default,
    block: Palette.Builder.() -> Unit = {},
): PaletteResult {
    var palette: PaletteResult by remember(this) { mutableStateOf(PaletteResult.Loading) }

    LaunchedEffect(this, coroutineContext, block) {
        palette =
            try {
                val result = generatePalette(coroutineContext, block)
                PaletteResult.Success(result)
            } catch (cause: Exception) {
                if (cause is CancellationException) throw cause
                PaletteResult.Error(cause)
            }
    }

    return palette
}

/**
 * Generates and remembers a [Palette] from an [ImageBitmap] loaded by the provided loader.
 *
 * This composable first loads the bitmap using the provided loader, then generates
 * a palette from it. The result is remembered across recompositions. The result
 * starts as [PaletteResult.Loading] and transitions to either [PaletteResult.Success]
 * or [PaletteResult.Error].
 *
 * @param[loader] A suspend lambda that loads and returns an [ImageBitmap].
 * @param[coroutineContext] The [CoroutineContext] to use for palette generation. Defaults to [Dispatchers.Default].
 * @param[block] Optional lambda to configure the [Palette.Builder] before generation.
 * @return A [PaletteResult] representing the current state of palette generation.
 */
@Composable
public fun rememberGeneratePalette(
    loader: suspend () -> ImageBitmap,
    coroutineContext: CoroutineContext = Dispatchers.Default,
    block: Palette.Builder.() -> Unit = {},
): PaletteResult {
    var loaderState: PaletteResult by remember(loader) { mutableStateOf(PaletteResult.Loading) }
    var bitmap: ImageBitmap? by remember(loader) { mutableStateOf(null) }

    LaunchedEffect(loader) {
        try {
            bitmap = loader()
        } catch (cause: Exception) {
            if (cause is CancellationException) throw cause
            loaderState = PaletteResult.Error(cause)
        }
    }

    val paletteResult = bitmap?.rememberGeneratePalette(coroutineContext, block)

    return paletteResult ?: loaderState
}

/**
 * Represents the result of a palette generation operation.
 *
 * This sealed interface provides three possible states:
 * - [Success] when palette generation completes successfully
 * - [Loading] when palette generation is in progress
 * - [Error] when palette generation fails
 */
public sealed interface PaletteResult {
    /**
     * Represents a successful palette generation.
     *
     * @param[palette] The generated [Palette] containing color swatches.
     */
    public data class Success(
        val palette: Palette,
    ) : PaletteResult

    /**
     * Represents an in-progress palette generation operation.
     */
    public data object Loading : PaletteResult

    /**
     * Represents a failed palette generation operation.
     *
     * @param[cause] The [Exception] that caused the failure.
     */
    public data class Error(
        val cause: Exception,
    ) : PaletteResult

    /**
     * Convenience property to get the [Palette] if this result is [Success], or `null` otherwise.
     *
     * @return The [Palette] if successful, or `null` for [Loading] or [Error] states.
     */
    public val paletteOrNull: Palette?
        get() =
            when (this) {
                is Success -> palette
                else -> null
            }
}
