package com.kmpalette.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.materialkolor.rememberDynamicColorScheme

@Composable
fun ThemePreviewSection(
    seedColor: Color,
    modifier: Modifier = Modifier,
) {
    val lightScheme = rememberDynamicColorScheme(seedColor, isDark = false, isAmoled = false)
    val darkScheme = rememberDynamicColorScheme(seedColor, isDark = true, isAmoled = false)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ThemeCard(
            title = "Light Theme",
            colorScheme = lightScheme,
        )
        ThemeCard(
            title = "Dark Theme",
            colorScheme = darkScheme,
        )
    }
}

@Composable
fun ThemeCard(
    title: String,
    colorScheme: ColorScheme,
    modifier: Modifier = Modifier,
) {
    OutlinedCard(modifier = modifier.fillMaxWidth()) {
        MaterialTheme(colorScheme = colorScheme) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp),
                )
                ColorSchemePreview(colorScheme)
            }
        }
    }
}

@Composable
fun ColorSchemePreview(
    colorScheme: ColorScheme,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // Primary Group
        ColorGroupRow(
            colors = listOf(
                ColorPair("Primary", colorScheme.primary, colorScheme.onPrimary),
                ColorPair("OnPrim", colorScheme.onPrimary, colorScheme.primary),
                ColorPair("PriCont", colorScheme.primaryContainer, colorScheme.onPrimaryContainer),
                ColorPair("OnPriCt", colorScheme.onPrimaryContainer, colorScheme.primaryContainer),
            ),
        )

        // Secondary Group
        ColorGroupRow(
            colors = listOf(
                ColorPair("Secondary", colorScheme.secondary, colorScheme.onSecondary),
                ColorPair("OnSec", colorScheme.onSecondary, colorScheme.secondary),
                ColorPair("SecCont", colorScheme.secondaryContainer, colorScheme.onSecondaryContainer),
                ColorPair("OnSecCt", colorScheme.onSecondaryContainer, colorScheme.secondaryContainer),
            ),
        )

        // Tertiary Group
        ColorGroupRow(
            colors = listOf(
                ColorPair("Tertiary", colorScheme.tertiary, colorScheme.onTertiary),
                ColorPair("OnTer", colorScheme.onTertiary, colorScheme.tertiary),
                ColorPair("TerCont", colorScheme.tertiaryContainer, colorScheme.onTertiaryContainer),
                ColorPair("OnTerCt", colorScheme.onTertiaryContainer, colorScheme.tertiaryContainer),
            ),
        )

        // Error Group
        ColorGroupRow(
            colors = listOf(
                ColorPair("Error", colorScheme.error, colorScheme.onError),
                ColorPair("OnError", colorScheme.onError, colorScheme.error),
                ColorPair("ErrCont", colorScheme.errorContainer, colorScheme.onErrorContainer),
                ColorPair("OnErrCt", colorScheme.onErrorContainer, colorScheme.errorContainer),
            ),
        )

        // Surface Group
        ColorGroupRow(
            colors = listOf(
                ColorPair("Surface", colorScheme.surface, colorScheme.onSurface),
                ColorPair("OnSurf", colorScheme.onSurface, colorScheme.surface),
                ColorPair("SurfVar", colorScheme.surfaceVariant, colorScheme.onSurfaceVariant),
                ColorPair("OnSfVar", colorScheme.onSurfaceVariant, colorScheme.surfaceVariant),
            ),
        )
    }
}

private data class ColorPair(
    val name: String,
    val bg: Color,
    val fg: Color,
)

@Composable
private fun ColorGroupRow(
    colors: List<ColorPair>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        colors.forEach { pair ->
            ColorBox(
                name = pair.name,
                color = pair.bg,
                textColor = pair.fg,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun ColorBox(
    name: String,
    color: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(color)
            .padding(4.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = name,
            color = textColor,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            maxLines = 2,
            minLines = 2, // Ensure vertical alignment consistency
        )
    }
}
