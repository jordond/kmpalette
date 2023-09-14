package dev.jordond.kmpalette

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.jordond.kmpalette.palette.LibresPaletteScreen
import dev.jordond.kmpalette.palette.ResourcesPaletteScreen
import dev.jordond.kmpalette.theme.AppTheme

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
                Button(onClick = { navigator.push(ResourcesPaletteScreen()) }) {
                    Text("Palette - Resources")
                }
            }
        }
    }
}