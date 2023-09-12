package dev.jordond.kmpalette.loader

import androidx.compose.ui.graphics.ImageBitmap
import io.github.skeptick.libres.images.Image

/**
 * Converts a Libres [Image] into a Compose [ImageBitmap].
 *
 * **Warning:** This could potentially take a long time depending on the size.
 */
internal expect suspend fun Image.toImageBitmap(): ImageBitmap