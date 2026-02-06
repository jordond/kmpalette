package com.kmpalette

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.kmpalette.internal.LruCache
import com.kmpalette.loader.DefaultImageBitmapLoader
import com.kmpalette.loader.ImageBitmapLoader
import com.kmpalette.loader.PainterLoader
import com.kmpalette.palette.graphics.Palette
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * Creates a [PaletteState] that will be remembered across compilation and can be used to
 * generate a [Palette] from an [ImageBitmap].
 *
 * @param[cacheSize] The maximum number of [Palette]s to cache. If 0, no caching will be done.
 * @param[coroutineContext] The [CoroutineContext] to use for generating [Palette]s.
 * @param[builder] A lambda that will be applied to the [Palette.Builder] to customize the
 * generation of the [Palette].
 * @return A [PaletteState] that will be remembered across composition.
 */
@Composable
public fun rememberPaletteState(
    cacheSize: Int = DominantColorState.DEFAULT_CACHE_SIZE,
    coroutineContext: CoroutineContext = Dispatchers.Default,
    builder: Palette.Builder.() -> Unit = {},
): PaletteState<ImageBitmap> =
    rememberPaletteState(
        loader = DefaultImageBitmapLoader,
        cacheSize = cacheSize,
        coroutineContext = coroutineContext,
        builder = builder,
    )

/**
 * Creates a [PaletteState] that will be remembered across compilation and can be used to
 * generate a [Palette] from an [ImageBitmap] using [loader].
 *
 * @param[T] The type of the input to load with [loader].
 * @param[loader] The [ImageBitmapLoader] to use for loading [T]s.
 * @param[cacheSize] The maximum number of [Palette]s to cache. If 0, no caching will be done.
 * @param[coroutineContext] The [CoroutineContext] to use for generating [Palette]s.
 * @param[builder] A lambda that will be applied to the [Palette.Builder] to customize the
 * generation of the [Palette].
 * @return A [PaletteState] that will be remembered across composition.
 */
@Composable
public fun <T : Any> rememberPaletteState(
    loader: ImageBitmapLoader<T>,
    cacheSize: Int = DominantColorState.DEFAULT_CACHE_SIZE,
    coroutineContext: CoroutineContext = Dispatchers.Default,
    builder: Palette.Builder.() -> Unit = {},
): PaletteState<T> =
    remember(loader) {
        object : PaletteState<T>(cacheSize, coroutineContext, builder) {
            override val loader: ImageBitmapLoader<T> = loader
        }
    }

/**
 * Wrapper around [rememberPaletteState] that uses [PainterLoader] to load the image.
 *
 * @see rememberPaletteState
 * @param[density] The [Density] used for drawing the [Painter] as [ImageBitmap].
 * @param[layoutDirection] The [LayoutDirection] used for drawing the [Painter] as [ImageBitmap].
 * @param[cacheSize] The maximum number of [Palette]s to cache. If 0, no caching will be done.
 * @param[coroutineContext] The [CoroutineContext] to use for generating [Palette]s.
 * @param[builder] A lambda that will be applied to the [Palette.Builder] to customize the
 * generation of the [Palette].
 * @return A [PaletteState] that will be remembered across composition.
 */
@Composable
public fun rememberPainterPaletteState(
    density: Density = LocalDensity.current,
    layoutDirection: LayoutDirection = LocalLayoutDirection.current,
    cacheSize: Int = DominantColorState.DEFAULT_CACHE_SIZE,
    coroutineContext: CoroutineContext = Dispatchers.Default,
    builder: Palette.Builder.() -> Unit = {},
): PaletteState<Painter> =
    rememberPaletteState(
        loader = PainterLoader(density, layoutDirection),
        cacheSize = cacheSize,
        coroutineContext = coroutineContext,
        builder = builder,
    )

/**
 * A state object that generates a [Palette] from an [ImageBitmap] using [loader].
 *
 * @param[T] The type of the input to load with [loader].
 * @param[cacheSize] The maximum number of [Palette]s to cache. If 0, no caching will be done.
 * @param[coroutineContext] The [CoroutineContext] to use for generating [Palette]s.
 * @param[builder] A lambda that will be applied to the [Palette.Builder] to customize the
 * generation of the [Palette].
 */
@Stable
public abstract class PaletteState<T : Any>(
    cacheSize: Int = DEFAULT_CACHE_SIZE,
    private val coroutineContext: CoroutineContext = Dispatchers.Default,
    private val builder: Palette.Builder.() -> Unit = {},
) {
    protected abstract val loader: ImageBitmapLoader<T>

    /**
     * The last [PaletteResult] of the [Palette] generation.
     */
    public var state: PaletteResult? by mutableStateOf(null)
        private set

    /**
     * The last [Palette] that was generated.
     */
    public val palette: Palette?
        get() =
            when (val state = state) {
                is PaletteResult.Success -> state.palette
                else -> null
            }

    private val cache =
        when {
            cacheSize > 0 -> LruCache<T, Palette>(cacheSize)
            else -> null
        }

    /**
     * Generates a [Palette] from [input] using [loader].
     *
     * If [input] is already cached, the cached [Palette] will be returned.
     *
     * @param[input] The input to load with [loader].
     */
    public suspend fun generate(input: T) {
        val cached = cache?.get(input)
        if (cached != null) {
            state = PaletteResult.Success(cached)
            return
        }

        state = PaletteResult.Loading
        state = generatePalette(input, loader)
    }

    /**
     * Resets the [PaletteState] to its initial state and clears the cache if set.
     */
    public fun reset() {
        state = null
        cache?.evictAll()
    }

    private suspend fun generatePalette(
        input: T,
        loader: ImageBitmapLoader<T>,
    ): PaletteResult {
        val bitmap =
            try {
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
