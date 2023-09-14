package dev.jordond.kmpalette.palette

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.screen.Screen
import dev.jordond.kmpalette.loader.ResourceLoader
import dev.jordond.kmpalette.rememberPaletteState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.resource

private val ImageStrings = listOf(
    "bg_1.jpg",
    "bg_2.jpg",
    "bg_3.jpg",
)

class ResourcesPaletteScreen : Screen {

    @Composable
    override fun Content() {
        val images = remember {
            ImageStrings.map { resource(it) }
        }

        PaletteScreen(
            images = images,
            paletteState = rememberPaletteState(ResourceLoader),
            painterResource = { index, _ ->
                painterResource(ImageStrings[index])
            }
        )
    }
}