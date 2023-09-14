package dev.jordond.kmpalette

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator

@Composable
internal fun App() {
    Navigator(HomeScreen())
}

