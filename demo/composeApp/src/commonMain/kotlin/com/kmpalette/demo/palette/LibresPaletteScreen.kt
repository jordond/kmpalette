package com.kmpalette.demo.palette

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.kmpalette.demo.R
import com.kmpalette.loader.LibresLoader
import com.kmpalette.rememberPaletteState
import io.github.skeptick.libres.compose.painterResource

internal val images = listOf(
    R.image.bg_1,
    R.image.bg_2,
    R.image.bg_3,
    R.image.bg_4,
    R.image.bg_5,
    R.image.bg_6,
    R.image.bg_7,
    R.image.bg_8,
    R.image.bg_9,
    R.image.bg_10,
)

class LibresPaletteScreen : Screen {

    @Composable
    override fun Content() {
        PaletteScreen(
            images = images,
            paletteState = rememberPaletteState(LibresLoader),
            painterResource = { _, image -> image.painterResource() }
        )
    }
}