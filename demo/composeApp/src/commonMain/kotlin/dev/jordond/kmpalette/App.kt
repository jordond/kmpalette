package dev.jordond.kmpalette

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.jordond.kmpalette.palette.graphics.Palette
import dev.jordond.kmpalette.theme.AppTheme
import korlibs.image.bitmap.Bitmap

@Composable
internal fun App() = AppTheme {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        val bitmap: Bitmap? = null
        Palette.from(bitmap!!).generate()
    }
}

internal expect fun openUrl(url: String?)