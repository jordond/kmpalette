package com.kmpalette.demo.palette

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.screen.Screen
import com.kmpalette.loader.ResourceLoader
import com.kmpalette.rememberPaletteState
import kmpalette.demo.composeapp.generated.resources.Res
import kmpalette.demo.composeapp.generated.resources.bg_1
import kmpalette.demo.composeapp.generated.resources.bg_2
import kmpalette.demo.composeapp.generated.resources.bg_3
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.painterResource

private val Images = listOf(
    Res.drawable.bg_1,
    Res.drawable.bg_2,
    Res.drawable.bg_3,
)

class ResourcesPaletteScreen : Screen {

    @Composable
    override fun Content() {
        val bitmaps = Images.map { imageResource(it) }
        PaletteScreen(
            images = bitmaps,
            paletteState = rememberPaletteState(),
            painterResource = { index, _ ->
                painterResource(Images[index])
            }
        )
    }
}