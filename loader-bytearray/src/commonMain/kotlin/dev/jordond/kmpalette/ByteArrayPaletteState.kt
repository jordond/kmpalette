package dev.jordond.kmpalette

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import dev.jordond.kmpalette.dominant.DominantColorState.Companion.DEFAULT_CACHE_SIZE
import dev.jordond.kmpalette.loader.ByteArrayLoader
import dev.jordond.kmpalette.loader.ImageBitmapLoader
import dev.jordond.kmpalette.palette.graphics.Palette
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

@Composable
public fun rememberByteArrayPaletteState(
    cacheSize: Int = DEFAULT_CACHE_SIZE,
    coroutineContext: CoroutineContext = Dispatchers.Default,
    builder: Palette.Builder.() -> Unit = {},
): ByteArrayPaletteState = remember {
    ByteArrayPaletteState(cacheSize, coroutineContext, builder)
}

@Stable
public class ByteArrayPaletteState(
    cacheSize: Int = DEFAULT_CACHE_SIZE,
    coroutineContext: CoroutineContext = Dispatchers.Default,
    builder: Palette.Builder.() -> Unit = {},
) : PaletteState<ByteArray>(
    cacheSize = cacheSize,
    coroutineContext = coroutineContext,
    builder = builder,
) {

    override val loader: ImageBitmapLoader<ByteArray> = ByteArrayLoader
}