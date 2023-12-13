package com.kmpalette.demo.dominant

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.screen.Screen
import co.touchlab.kermit.Logger
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.kmpalette.loader.rememberPainterLoader
import com.kmpalette.rememberDominantColorState
import io.ktor.http.Url

actual class HardwareBitmapScreen actual constructor(): Screen {

    private val demoImageUrl = Url("https://picsum.photos/600/300")

    @Composable
    override fun Content() {
        val context = LocalContext.current
        val request = remember {
            ImageRequest.Builder(context)
                .data(demoImageUrl)
                .allowHardware(enable = true)
                .build()
        }
        val painter = rememberAsyncImagePainter(request)
        when(painter.state) {
            is AsyncImagePainter.State.Empty -> Text("Empty")
            is AsyncImagePainter.State.Error -> Text("Failed to load image")
            is AsyncImagePainter.State.Loading -> CircularProgressIndicator()
            is AsyncImagePainter.State.Success -> SuccessContent(painter)
        }
    }

    @Composable
    private fun SuccessContent(painter: AsyncImagePainter) {
        val loader = rememberPainterLoader()
        val dominantColorState = rememberDominantColorState(loader)
        LaunchedEffect(painter) {
            dominantColorState.updateFrom(painter)

            Logger.d("Result = ${dominantColorState.result}")
        }

        DominantDemoContent(
            dominantColorState = dominantColorState,
            painter = painter,
        )
    }
}