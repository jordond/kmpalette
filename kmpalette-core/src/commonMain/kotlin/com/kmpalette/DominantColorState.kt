package com.kmpalette

import androidx.collection.LruCache
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.palette.graphics.Palette
import com.kmpalette.loader.ImageBitmapLoader
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * A class which stores and caches the result of any calculated dominant colors.
 *
 * @property[color] The dominant color.
 * @property[onColor] The color to use _on_ [color].
 */
@Immutable
private data class DominantColors(
    val color: Color,
    val onColor: Color,
)

/**
 * Create a [DominantColorState] which will be remembered across compositions. Then can be used
 * to generate a dominant color from an [ImageBitmap].
 *
 * @param[defaultColor] The default color, which will be used if [Palette.generate] fails.
 * @param[defaultOnColor] The default color to use _on_ [defaultColor].
 * @param[cacheSize] The size of the [LruCache] used to store recent results. Pass `0` to disable.
 * @param[coroutineContext] The [CoroutineContext] used to launch the coroutine.
 * @param[isColorValid] A lambda which allows filtering of the calculated image colors.
 * @param[builder] A lambda which allows customization of the [Palette.Builder] used to generate
 * the [Palette].
 * @return A [DominantColorState] which can be used to generate a dominant color
 * from a [ImageBitmap].
 */
@Composable
public fun rememberDominantColorState(
    defaultColor: Color = MaterialTheme.colorScheme.primary,
    defaultOnColor: Color = MaterialTheme.colorScheme.onPrimary,
    cacheSize: Int = 0,
    coroutineContext: CoroutineContext = Dispatchers.Default,
    isColorValid: (Color) -> Boolean = { true },
    builder: Palette.Builder.() -> Unit = {},
): DominantColorState<ImageBitmap> = rememberDominantColorState(
    loader = ImageBitmapLoader,
    defaultColor = defaultColor,
    defaultOnColor = defaultOnColor,
    cacheSize = cacheSize,
    coroutineContext = coroutineContext,
    isColorValid = isColorValid,
    builder = builder,
)

/**
 * Create a [DominantColorState] which will be remembered across compositions. Then can be used
 * to generate a dominant color from an [ImageBitmap].
 *
 * @param[loader] The [ImageBitmapLoader] to use to load the [ImageBitmap].
 * @param[defaultColor] The default color, which will be used if [Palette.generate] fails.
 * @param[defaultOnColor] The default color to use _on_ [defaultColor].
 * @param[cacheSize] The size of the [LruCache] used to store recent results. Pass `0` to disable.
 * @param[coroutineContext] The [CoroutineContext] used to launch the coroutine.
 * @param[isColorValid] A lambda which allows filtering of the calculated image colors.
 * @param[builder] A lambda which allows customization of the [Palette.Builder] used to generate
 * the [Palette].
 * @return A [DominantColorState] which can be used to generate a dominant color
 * from a [ImageBitmap].
 */
@Composable
public fun <T : Any> rememberDominantColorState(
    loader: ImageBitmapLoader<T>,
    defaultColor: Color = MaterialTheme.colorScheme.primary,
    defaultOnColor: Color = MaterialTheme.colorScheme.onPrimary,
    cacheSize: Int = DominantColorState.DEFAULT_CACHE_SIZE,
    coroutineContext: CoroutineContext = Dispatchers.Default,
    isColorValid: (Color) -> Boolean = { true },
    builder: Palette.Builder.() -> Unit = {},
): DominantColorState<T> = remember(loader, defaultColor, defaultOnColor) {
    object : DominantColorState<T>(
        defaultColor = defaultColor,
        defaultOnColor = defaultOnColor,
        cacheSize = cacheSize,
        coroutineContext = coroutineContext,
        isColorValid = isColorValid,
        builder = builder,
    ) {
        override val loader: ImageBitmapLoader<T> = loader
    }
}


/**
 * A class which stores and caches the result of any calculated dominant colors.
 *
 * Access the current dominant color via [color].
 *
 * @param[T] The type of the input to calculate the dominant color from.
 * @param[defaultColor] The default color, which will be used if [calculateDominantColor] fails to
 * calculate a dominant color
 * @param[cacheSize] The size of the [LruCache] used to store recent results. Pass `0` to
 * disable the cache.
 * @param[coroutineContext] The [CoroutineContext] used to launch the coroutine.
 * @param[isColorValid] A lambda which allows filtering of the calculated image colors.
 * @param[builder] A lambda which allows customization of the [Palette.Builder] used to generate
 * the [Palette] from the input [T]
 */
@Suppress("MemberVisibilityCanBePrivate")
@Stable
public abstract class DominantColorState<T : Any>(
    private val defaultColor: Color,
    private val defaultOnColor: Color,
    cacheSize: Int = DEFAULT_CACHE_SIZE,
    private val coroutineContext: CoroutineContext = Dispatchers.Default,
    private val isColorValid: (Color) -> Boolean = { true },
    private val builder: Palette.Builder.() -> Unit = {},
) {

    protected abstract val loader: ImageBitmapLoader<T>

    /**
     * The dominant color.
     */
    public var color: Color by mutableStateOf(defaultColor)
        private set

    /**
     * The color to use _on_ [color].
     */
    public var onColor: Color by mutableStateOf(defaultColor)
        private set

    /**
     * The result of the last [Palette] generation.
     */
    public var result: PaletteResult? by mutableStateOf(null)
        private set

    private val cache = when {
        cacheSize > 0 -> LruCache<T, DominantColors>(cacheSize)
        else -> null
    }

    /**
     * Update the dominant color from the given [input].
     *
     * @param[input] The input to calculate the dominant color from.
     */
    public suspend fun updateFrom(input: T) {
        val result = calculateDominantColor(input, loader)
        color = result?.color ?: defaultColor
        onColor = result?.onColor ?: defaultOnColor
    }

    private suspend fun calculateDominantColor(
        input: T,
        loader: ImageBitmapLoader<T>,
    ): DominantColors? {
        val cached = cache?.get(input)
        if (cached != null) {
            return cached
        }

        this.result = PaletteResult.Loading

        return calculateSwatchesInImage(input, loader, builder)
            .sortedByDescending { swatch -> swatch.population }
            .firstOrNull { swatch -> isColorValid(Color(swatch.rgb)) }
            ?.let { swatch ->
                DominantColors(
                    color = Color(swatch.rgb),
                    onColor = Color(swatch.bodyTextColor).copy(alpha = 1f),
                )
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
            result = PaletteResult.Error(cause)
            return emptyList()
        }

        return try {
            val palette = bitmap.generatePalette(coroutineContext, block)
            this.result = PaletteResult.Success(palette)
            palette.swatches
        } catch (cause: Throwable) {
            if (cause is CancellationException) throw cause
            result = PaletteResult.Error(cause)
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

        public const val DEFAULT_CACHE_SIZE: Int = 5
    }
}
