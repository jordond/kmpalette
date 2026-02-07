package com.kmpalette

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.window.core.layout.WindowSizeClass
import com.kmpalette.navigation.AppDestination
import com.kmpalette.theme.AppTheme
import com.kmpalette.theme.Seed
import com.kmpalette.ui.screens.PaletteScreen
import com.kmpalette.ui.screens.SettingsScreen

@Preview
@Composable
fun App(onThemeChanged: @Composable (isDark: Boolean) -> Unit = {}) {
    var dominantColor by remember { mutableStateOf<Color?>(null) }
    var selectedDestination by remember { mutableStateOf(AppDestination.Palette) }

    AppTheme(
        seed = dominantColor ?: Seed,
    ) {
        val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
        val useNavigationRail = windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)

        if (useNavigationRail) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing),
            ) {
                NavigationRail(
                    modifier = Modifier.fillMaxHeight(),
                ) {
                    AppDestination.entries.forEach { destination ->
                        NavigationRailItem(
                            icon = {
                                Icon(
                                    imageVector = if (selectedDestination == destination) {
                                        destination.selectedIcon
                                    } else {
                                        destination.unselectedIcon
                                    },
                                    contentDescription = destination.label,
                                )
                            },
                            label = { Text(destination.label) },
                            selected = selectedDestination == destination,
                            onClick = { selectedDestination = destination },
                        )
                    }
                }

                AppContent(
                    selectedDestination = selectedDestination,
                    onDominantColorChanged = { dominantColor = it },
                    modifier = Modifier.weight(1f),
                )
            }
        } else {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing),
                bottomBar = {
                    NavigationBar {
                        AppDestination.entries.forEach { destination ->
                            NavigationBarItem(
                                icon = {
                                    Icon(
                                        imageVector = if (selectedDestination == destination) {
                                            destination.selectedIcon
                                        } else {
                                            destination.unselectedIcon
                                        },
                                        contentDescription = destination.label,
                                    )
                                },
                                label = { Text(destination.label) },
                                selected = selectedDestination == destination,
                                onClick = { selectedDestination = destination },
                            )
                        }
                    }
                },
            ) { paddingValues ->
                AppContent(
                    selectedDestination = selectedDestination,
                    onDominantColorChanged = { dominantColor = it },
                    modifier = Modifier.padding(paddingValues),
                )
            }
        }
    }
}

@Composable
private fun AppContent(
    selectedDestination: AppDestination,
    onDominantColorChanged: (Color?) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (selectedDestination) {
        AppDestination.Palette -> {
            PaletteScreen(
                onDominantColorChanged = onDominantColorChanged,
                modifier = modifier.fillMaxSize(),
            )
        }
        AppDestination.Settings -> {
            SettingsScreen(
                modifier = modifier.fillMaxSize(),
            )
        }
    }
}
