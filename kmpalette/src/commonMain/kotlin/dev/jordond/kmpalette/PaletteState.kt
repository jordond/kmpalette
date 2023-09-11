package dev.jordond.kmpalette

import androidx.collection.LruCache
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dev.jordond.kmpalette.loader.ImageBitmapLoader
import dev.jordond.kmpalette.palette.graphics.Palette
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

@Stable
public abstract class PaletteState<T : Any>(
    cacheSize: Int = DEFAULT_CACHE_SIZE,
    private val coroutineContext: CoroutineContext = Dispatchers.Default,
    private val builder: Palette.Builder.() -> Unit = {},
) {

    protected abstract val loader: ImageBitmapLoader<T>

    public var state: PaletteResult? by mutableStateOf(null)
        private set

    public val palette: Palette?
        get() = when (val state = state) {
            is PaletteResult.Success -> state.palette
            else -> null
        }

    private val cache = when {
        cacheSize > 0 -> LruCache<T, Palette>(cacheSize)
        else -> null
    }

    public suspend fun generate(input: T) {
        val cached = cache?.get(input)
        if (cached != null) {
            state = PaletteResult.Success(cached)
            return
        }

        state = PaletteResult.Loading
        state = generatePalette(input, loader)
    }

    private suspend fun generatePalette(input: T, loader: ImageBitmapLoader<T>): PaletteResult {
        val bitmap = try {
            loader.load(input)
        } catch (cause: Throwable) {
            if (cause is CancellationException) throw cause
            return PaletteResult.Error(cause)
        }

        return try {
            val palette = bitmap.generatePalette(coroutineContext, builder)
            PaletteResult.Success(palette)
        } catch (cause: Throwable) {
            if (cause is CancellationException) throw cause
            PaletteResult.Error(cause)
        }
    }

    public companion object {

        public const val DEFAULT_CACHE_SIZE: Int = 6
    }
}