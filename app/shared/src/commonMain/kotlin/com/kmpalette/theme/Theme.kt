package com.kmpalette.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.materialkolor.rememberDynamicColorScheme

internal val LocalThemeIsDark = compositionLocalOf { mutableStateOf(true) }

/**
 * Wrapper that allows toggling between dynamic and static themes.
 */
@Composable
fun AppTheme(
    seed: Color,
    content: @Composable () -> Unit,
) {
    val systemIsDark = isSystemInDarkTheme()
    val isDarkState = remember(systemIsDark) { mutableStateOf(systemIsDark) }

    CompositionLocalProvider(
        LocalThemeIsDark provides isDarkState,
    ) {
        val isDark by isDarkState

        val dynamicColorScheme = rememberDynamicColorScheme(
            seedColor = seed,
            isDark = isDark,
            isAmoled = false,
        )

        MaterialTheme(
            colorScheme = dynamicColorScheme,
            content = { Surface(content = content) },
        )
    }
}
