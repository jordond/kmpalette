package com.kmpalette.demo

import android.app.Activity
import android.app.Application
import android.graphics.Color
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import java.io.File

class AndroidApp : Application() {

    companion object {

        lateinit var INSTANCE: AndroidApp

        fun sampleFile(): File = File(INSTANCE.cacheDir, "sample_image.jpg")
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        assets.openFd("sample_image.jpg").use { assetFd ->
            sampleFile().outputStream().use { fileOut ->
                assetFd.createInputStream().use { fileIn ->
                    fileIn.copyTo(fileOut)
                }
            }
        }
    }
}

class AppActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val systemBarColor = Color.parseColor("#80000000")
        setContent {
            val view = LocalView.current
            if (!view.isInEditMode) {
                SideEffect {
                    val window = (view.context as Activity).window
                    WindowCompat.setDecorFitsSystemWindows(window, false)
                    window.statusBarColor = systemBarColor
                    window.navigationBarColor = systemBarColor
                }
            }
            App()
        }
    }
}
