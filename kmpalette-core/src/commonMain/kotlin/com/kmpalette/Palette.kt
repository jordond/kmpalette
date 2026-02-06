package com.kmpalette

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toPixelMap
import com.kmpalette.palette.graphics.Palette
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

public fun Palette.Companion.from(bitmap: ImageBitmap): Palette.Builder {
    val pixels = IntArray(bitmap.width * bitmap.height)
    bitmap.readPixels(pixels)
    return Palette.from(pixels, bitmap.width, bitmap.height)
}

public suspend fun ImageBitmap.generatePalette(
    coroutineContext: CoroutineContext = Dispatchers.Default,
    block: Palette.Builder.() -> Unit = {},
): Palette =
    withContext(coroutineContext) {
        Palette.from(bitmap = this@generatePalette).apply(block).generate()
    }

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
            } catch (cause: Throwable) {
                if (cause is CancellationException) throw cause
                PaletteResult.Error(cause)
            }
    }

    return palette
}

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

public sealed interface PaletteResult {
    public data class Success(
        val palette: Palette,
    ) : PaletteResult

    public data object Loading : PaletteResult

    public data class Error(
        val cause: Throwable,
    ) : PaletteResult

    public val paletteOrNull: Palette?
        get() =
            when (this) {
                is Success -> palette
                else -> null
            }
}
