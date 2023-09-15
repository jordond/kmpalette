package dev.jordond.kmpalette.dominant

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import cafe.adriel.voyager.core.screen.Screen
import dev.jordond.kmpalette.loader.NetworkLoader
import dev.jordond.kmpalette.rememberDominantColorState
import io.ktor.http.Url

class NetworkDemoScreen : Screen {

    private val demoImageUrl = Url("https://picsum.photos/600/300")

    @Composable
    override fun Content() {
        val loader = remember { NetworkLoader() }
        val dominantColorState = rememberDominantColorState(loader)
        var errorMessage: String? by remember { mutableStateOf(null) }
        var image: ImageBitmap? by remember { mutableStateOf(null) }
        LaunchedEffect(demoImageUrl) {
            try {
                image = loader.load(demoImageUrl)
            } catch (cause: Throwable) {
                cause.printStackTrace()
                errorMessage = cause.message
            }
            dominantColorState.updateFrom(demoImageUrl)
        }

        DominantDemoContent(
            dominantColorState = dominantColorState,
            imageBitmap = image,
        ) {
            if (errorMessage != null) {
                Text(errorMessage!!)
            }
        }
    }
}