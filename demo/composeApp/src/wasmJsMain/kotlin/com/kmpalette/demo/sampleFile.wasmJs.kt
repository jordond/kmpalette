package com.kmpalette.demo

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

actual fun sampleFile(): String {
    error("Not supported by this platform")
}

actual class FileDemoScreen actual constructor() : Screen {

    @Composable
    override fun Content() {
        Text("Not supported by this platform")
    }
}