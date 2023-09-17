package com.kmpalette.demo

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.navigator.Navigator

internal data class SelectedColor(val name: String, val color: Color)

internal val defaultColor = SelectedColor("default", Color(0xFF1976D2))

@Composable
internal fun App() {
    Navigator(HomeScreen())
}

