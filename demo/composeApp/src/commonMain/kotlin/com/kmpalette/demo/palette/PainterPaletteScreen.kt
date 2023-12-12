package com.kmpalette.demo.palette

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.kmpalette.loader.rememberPainterLoader
import com.kmpalette.rememberPaletteState
import io.github.skeptick.libres.compose.painterResource

class PainterPaletteScreen : Screen {

    @Composable
    override fun Content() {
        val painters = images.map { it.painterResource() }

        val loader = rememberPainterLoader()
        PaletteScreen(
            images = painters,
            paletteState = rememberPaletteState(loader),
            painterResource = { _, painter -> painter },
        )
    }
}