package dev.jordond.kmpalette

import androidx.compose.runtime.Composable
import dev.jordond.kmpalette.DominantColorState.Companion.DEFAULT_CACHE_SIZE
import dev.jordond.kmpalette.loader.ResourceLoader
import dev.jordond.kmpalette.palette.graphics.Palette
import kotlinx.coroutines.Dispatchers
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Resource
import kotlin.coroutines.CoroutineContext

@Composable
@ExperimentalResourceApi
public fun rememberResourcePaletteState(
    cacheSize: Int = DEFAULT_CACHE_SIZE,
    coroutineContext: CoroutineContext = Dispatchers.Default,
    builder: Palette.Builder.() -> Unit = {},
): PaletteState<Resource> = rememberPaletteState(
    loader = ResourceLoader,
    cacheSize = cacheSize,
    coroutineContext = coroutineContext,
    builder = builder,
)
