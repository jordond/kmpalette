package com.kmpalette.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.kmpalette.demo.dominant.Base64DemoScreen
import com.kmpalette.demo.dominant.DominantPhotoColorScreen
import com.kmpalette.demo.dominant.NetworkDemoScreen
import com.kmpalette.demo.dominant.NetworkPainterDemoScreen
import com.kmpalette.demo.palette.LibresPaletteScreen
import com.kmpalette.demo.palette.PainterPaletteScreen
import com.kmpalette.demo.theme.AppTheme

class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        AppTheme(seedColor = Color.Red) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize(),
            ) {
                Button(onClick = { navigator.push(LibresPaletteScreen()) }) {
                    Text("Palette - Libres")
                }
                Button(onClick = { navigator.push(PainterPaletteScreen()) }) {
                    Text("Palette - Painter")
                }
                Button(onClick = { navigator.push(NetworkPainterDemoScreen()) }) {
                    Text("Dominant Color - Painter")
                }
                Button(onClick = { navigator.push(DominantPhotoColorScreen()) }) {
                    Text("Dominant Color - Photo Picker")
                }
                Button(onClick = { navigator.push(Base64DemoScreen()) }) {
                    Text("Dominant Color - Base64")
                }
                Button(onClick = { navigator.push(NetworkDemoScreen()) }) {
                    Text("Dominant Color - Network")
                }
                Button(onClick = { navigator.push(FileDemoScreen()) }) {
                    Text("Dominant Color - Sample File")
                }
            }
        }
    }
}