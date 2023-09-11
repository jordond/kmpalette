package dev.jordond.kmpalette.dominant

import androidx.collection.LruCache
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import dev.jordond.kmpalette.PaletteResult
import dev.jordond.kmpalette.generatePalette
import dev.jordond.kmpalette.loader.ImageBitmapLoader
import dev.jordond.kmpalette.palette.graphics.Palette
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

@Immutable
private data class DominantColors(
    val color: Color,
)

/**
 * A class which stores and caches the result of any calculated dominant colors
 * from images.
 *
 * @param[defaultColor] The default color, which will be used if [calculateDominantColor] fails to
 * calculate a dominant color
 * @param[cacheSize] The size of the [LruCache] used to store recent results. Pass `0` to
 * disable the cache.
 * @param[isColorValid] A lambda which allows filtering of the calculated image colors.
 */
@Stable
public abstract class DominantColorState<T : Any>(
    private val defaultColor: Color,
    cacheSize: Int = DEFAULT_CACHE_SIZE,
    private val coroutineContext: CoroutineContext = Dispatchers.Default,
    private val isColorValid: (Color) -> Boolean = { true },
    private val builder: Palette.Builder.() -> Unit = {},
) {

    protected abstract val loader: ImageBitmapLoader<T>

    public var color: Color by mutableStateOf(defaultColor)
        private set

    public var palette: PaletteResult? by mutableStateOf(null)
        private set


    private val cache = when {
        cacheSize > 0 -> LruCache<T, DominantColors>(cacheSize)
        else -> null
    }

    public suspend fun updateFrom(input: T) {
        palette = PaletteResult.Loading
        val result = calculateDominantColor(input, loader)
        color = result?.color ?: defaultColor
    }

    private suspend fun calculateDominantColor(input: T, loader: ImageBitmapLoader<T>): DominantColors? {
        val cached = cache?.get(input)
        if (cached != null) {
            return cached
        }

        return calculateSwatchesInImage(input, loader, builder)
            .sortedByDescending { swatch -> swatch.population }
            .firstOrNull { swatch -> isColorValid(Color(swatch.rgb)) }
            ?.let { swatch ->
                DominantColors(color = Color(swatch.rgb))
            }
            ?.also { result -> cache?.put(input, result) }
    }

    private suspend fun <T> calculateSwatchesInImage(
        input: T,
        loader: ImageBitmapLoader<T>,
        block: Palette.Builder.() -> Unit,
    ): List<Palette.Swatch> {
        val bitmap = try {
            loader.load(input)
        } catch (cause: Throwable) {
            if (cause is CancellationException) throw cause
            palette = PaletteResult.Error(cause)
            return emptyList()
        }

        return try {
            val palette = bitmap.generatePalette(coroutineContext, block)
            this.palette = PaletteResult.Success(palette)
            palette.swatches
        } catch (cause: Throwable) {
            if (cause is CancellationException) throw cause
            palette = PaletteResult.Error(cause)
            return emptyList()
        }
    }

    /**
     * Reset the color values to [defaultColor].
     */
    public fun reset() {
        color = defaultColor
    }

    public companion object {

        public const val DEFAULT_CACHE_SIZE: Int = 12
    }
}

internal fun List<Palette.Swatch>.dominant(): Palette.Swatch? {
    return maxByOrNull { it.population }
}
