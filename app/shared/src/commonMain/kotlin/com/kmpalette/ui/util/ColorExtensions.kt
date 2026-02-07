package com.kmpalette.ui.util

import androidx.compose.ui.graphics.Color

/**
 * Converts a Color to its hex string representation (e.g., "#6750A4").
 */
fun Color.toHexString(): String {
    val red = (this.red * 255).toInt()
    val green = (this.green * 255).toInt()
    val blue = (this.blue * 255).toInt()
    return "#${red.toHex()}${green.toHex()}${blue.toHex()}"
}

private fun Int.toHex(): String = this.toString(16).padStart(2, '0').uppercase()
