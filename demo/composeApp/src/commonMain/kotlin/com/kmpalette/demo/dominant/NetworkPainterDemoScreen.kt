package com.kmpalette.demo.dominant

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.painter.Painter
import cafe.adriel.voyager.core.screen.Screen
import com.kmpalette.loader.rememberPainterLoader
import com.kmpalette.rememberDominantColorState
import io.kamel.core.Resource
import io.kamel.image.asyncPainterResource
import io.ktor.http.Url

class NetworkPainterDemoScreen : Screen {

    private val demoImageUrl = Url("https://picsum.photos/600/300")

    @Composable
    override fun Content() {
        when (val painterResource = asyncPainterResource(demoImageUrl)) {
            is Resource.Failure -> Text("Failed to load image")
            is Resource.Loading -> CircularProgressIndicator()
            is Resource.Success -> SuccessContent(painterResource)
        }
    }

    @Composable
    private fun SuccessContent(resource: Resource.Success<Painter>) {
        val loader = rememberPainterLoader()
        val dominantColorState = rememberDominantColorState(loader)
        LaunchedEffect(resource) {
            dominantColorState.updateFrom(resource.value)
        }

        DominantDemoContent(
            dominantColorState = dominantColorState,
            painter = resource.value,
        )
    }
}