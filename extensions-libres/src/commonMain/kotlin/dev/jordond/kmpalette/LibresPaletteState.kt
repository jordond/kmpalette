package dev.jordond.kmpalette

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import dev.jordond.kmpalette.dominant.DominantColorState.Companion.DEFAULT_CACHE_SIZE
import dev.jordond.kmpalette.loader.ImageBitmapLoader
import dev.jordond.kmpalette.loader.LibresLoader
import dev.jordond.kmpalette.palette.graphics.Palette
import io.github.skeptick.libres.images.Image
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

@Composable
public fun rememberLibresPaletteState(
    cacheSize: Int = DEFAULT_CACHE_SIZE,
    coroutineContext: CoroutineContext = Dispatchers.Default,
    builder: Palette.Builder.() -> Unit = {},
): LibresPaletteState = remember {
    LibresPaletteState(cacheSize, coroutineContext, builder)
}

@Stable
public class LibresPaletteState(
    cacheSize: Int = DEFAULT_CACHE_SIZE,
    coroutineContext: CoroutineContext = Dispatchers.Default,
    builder: Palette.Builder.() -> Unit = {},
) : PaletteState<Image>(
    cacheSize = cacheSize,
    coroutineContext = coroutineContext,
    builder = builder,
) {

    override val loader: ImageBitmapLoader<Image> = LibresLoader
}