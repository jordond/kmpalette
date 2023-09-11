package dev.jordond.kmpalette

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import dev.jordond.kmpalette.dominant.DominantColorState.Companion.DEFAULT_CACHE_SIZE
import dev.jordond.kmpalette.loader.ImageBitmapLoader
import dev.jordond.kmpalette.loader.ResourceLoader
import dev.jordond.kmpalette.palette.graphics.Palette
import kotlinx.coroutines.Dispatchers
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Resource
import kotlin.coroutines.CoroutineContext

@Composable
@ExperimentalResourceApi
public fun rememberLibresPaletteState(
    cacheSize: Int = DEFAULT_CACHE_SIZE,
    coroutineContext: CoroutineContext = Dispatchers.Default,
    builder: Palette.Builder.() -> Unit = {},
): ResourcePaletteState = remember {
    ResourcePaletteState(cacheSize, coroutineContext, builder)
}

@Stable
@ExperimentalResourceApi
public class ResourcePaletteState(
    cacheSize: Int = DEFAULT_CACHE_SIZE,
    coroutineContext: CoroutineContext = Dispatchers.Default,
    builder: Palette.Builder.() -> Unit = {},
) : PaletteState<Resource>(
    cacheSize = cacheSize,
    coroutineContext = coroutineContext,
    builder = builder,
) {

    override val loader: ImageBitmapLoader<Resource> = ResourceLoader
}