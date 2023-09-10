package dev.jordond.kmpalette

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import dev.jordond.kmpalette.palette.graphics.Palette
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

public fun Palette.dominantSwatch(): Palette.Swatch =
    swatches.maxByOrNull { it.population } ?: error("No swatches found")

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
): Palette? {
    var palette: Palette? by remember(this) { mutableStateOf(null) }

    LaunchedEffect(this, coroutineContext, block) {
        palette = generatePalette(coroutineContext, block)
    }

    return palette
}