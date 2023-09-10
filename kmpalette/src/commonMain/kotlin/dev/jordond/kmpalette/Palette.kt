package dev.jordond.kmpalette

import androidx.compose.ui.graphics.ImageBitmap
import dev.jordond.kmpalette.palette.graphics.Palette
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

public fun Palette.dominantSwatch(): Palette.Swatch =
    swatches.maxByOrNull { it.population } ?: error("No swatches found")
