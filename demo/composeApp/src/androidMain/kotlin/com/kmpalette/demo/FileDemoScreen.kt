package com.kmpalette.demo

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import cafe.adriel.voyager.core.screen.Screen
import com.kmpalette.demo.dominant.DominantDemoContent
import com.kmpalette.loader.FilePathLoader
import com.kmpalette.rememberDominantColorState

actual class FileDemoScreen : Screen {

    @Composable
    override fun Content() {
        val filePath = remember { sampleFile() }
        val dominantColorState = rememberDominantColorState(loader = FilePathLoader) {
            clearFilters()
        }
        var errorMessage: String? by remember { mutableStateOf(null) }
        var image: ImageBitmap? by remember { mutableStateOf(null) }
        LaunchedEffect(filePath) {
            try {
                image = FilePathLoader.load(filePath)
            } catch (cause: Throwable) {
                cause.printStackTrace()
                errorMessage = cause.message
            }
            dominantColorState.updateFrom(filePath)
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