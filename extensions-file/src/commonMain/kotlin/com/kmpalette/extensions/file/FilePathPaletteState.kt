package com.kmpalette.extensions.file

import androidx.compose.runtime.Composable
import androidx.palette.graphics.Palette
import com.kmpalette.PaletteState
import com.kmpalette.loader.FilePathLoader
import com.kmpalette.loader.PathLoader
import com.kmpalette.rememberPaletteState
import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers
import okio.Path
import kotlin.coroutines.CoroutineContext

/**
 * Wrapper around [rememberPaletteState] that uses [FilePathLoader] to load the image.
 *
 * You must import the Ktor client library to use this loader.
 *
 * @see rememberPaletteState
 * @param[cacheSize] The maximum number of [Palette]s to cache. If 0, no caching will be done.
 * @param[coroutineContext] The [CoroutineContext] to use for generating [Palette]s.
 * @param[builder] A lambda that will be applied to the [Palette.Builder] to customize the
 * generation of the [Palette].
 * @return A [PaletteState] that will be remembered across composition.
 */
@Composable
public fun rememberFilePathPaletteState(
    cacheSize: Int = PaletteState.DEFAULT_CACHE_SIZE,
    httpClient: HttpClient = HttpClient(),
    coroutineContext: CoroutineContext = Dispatchers.Default,
    builder: Palette.Builder.() -> Unit = {},
): PaletteState<String> = rememberPaletteState(
    loader = FilePathLoader,
    cacheSize = cacheSize,
    coroutineContext = coroutineContext,
    builder = builder,
)

/**
 * Wrapper around [rememberPaletteState] that uses [PathLoader] to load the image.
 *
 * You must import the Ktor client library to use this loader.
 *
 * @see rememberPaletteState
 * @param[cacheSize] The maximum number of [Palette]s to cache. If 0, no caching will be done.
 * @param[coroutineContext] The [CoroutineContext] to use for generating [Palette]s.
 * @param[builder] A lambda that will be applied to the [Palette.Builder] to customize the
 * generation of the [Palette].
 * @return A [PaletteState] that will be remembered across composition.
 */
@Composable
public fun rememberPathPaletteState(
    cacheSize: Int = PaletteState.DEFAULT_CACHE_SIZE,
    httpClient: HttpClient = HttpClient(),
    coroutineContext: CoroutineContext = Dispatchers.Default,
    builder: Palette.Builder.() -> Unit = {},
): PaletteState<Path> = rememberPaletteState(
    loader = PathLoader,
    cacheSize = cacheSize,
    coroutineContext = coroutineContext,
    builder = builder,
)