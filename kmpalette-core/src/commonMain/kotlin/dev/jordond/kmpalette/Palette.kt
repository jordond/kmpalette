package dev.jordond.kmpalette

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import dev.jordond.kmpalette.palette.graphics.Palette
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

public suspend fun ImageBitmap.generatePalette(
    coroutineContext: CoroutineContext = Dispatchers.Default,
    block: Palette.Builder.() -> Unit = {},
): Palette {
    return withContext(coroutineContext) {
        Palette.from(bitmap = this@generatePalette).apply(block).generate()
    }
}

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

public sealed interface PaletteResult {

    public data class Success(val palette: Palette) : PaletteResult
    public data object Loading : PaletteResult
    public data class Error(val cause: Throwable) : PaletteResult

    public val paletteOrNull: Palette?
        get() = when (this) {
            is Success -> palette
            else -> null
        }
}