package dev.jordond.kmpalette

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import dev.jordond.kmpalette.loader.toImageBitmap
import dev.jordond.kmpalette.palette.graphics.Palette
import kotlinx.coroutines.Dispatchers
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Resource
import kotlin.coroutines.CoroutineContext

@Composable
@ExperimentalResourceApi
public fun Resource.rememberGeneratePalette(
    coroutineContext: CoroutineContext = Dispatchers.Default,
    block: Palette.Builder.() -> Unit = {},
): Palette? {
    var bitmap: ImageBitmap? by remember(this) { mutableStateOf(null) }
    LaunchedEffect(this) {
        bitmap = toImageBitmap()
    }

    return bitmap?.rememberGeneratePalette(coroutineContext, block)
}