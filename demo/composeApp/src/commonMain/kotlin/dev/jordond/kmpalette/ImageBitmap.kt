package dev.jordond.kmpalette

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import io.github.skeptick.libres.images.Image

@Composable
expect fun Image.toImageBitmap(): ImageBitmap