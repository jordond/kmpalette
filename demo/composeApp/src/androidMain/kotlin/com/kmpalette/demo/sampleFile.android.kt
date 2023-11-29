package com.kmpalette.demo

actual fun sampleFile(): String {
    return AndroidApp.sampleFile().absolutePath
}