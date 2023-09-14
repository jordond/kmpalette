package dev.jordond.kmpalette.util

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp

@Composable
fun colorSchemePairs() = listOf(
    "Primary" to (MaterialTheme.colorScheme.primary to MaterialTheme.colorScheme.onPrimary),
    "PrimaryContainer" to (MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer),
    "Secondary" to (MaterialTheme.colorScheme.secondary to MaterialTheme.colorScheme.onSecondary),
    "SecondaryContainer" to (MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer),
    "Tertiary" to (MaterialTheme.colorScheme.tertiary to MaterialTheme.colorScheme.onTertiary),
    "TertiaryContainer" to (MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.onTertiaryContainer),
    "Error" to (MaterialTheme.colorScheme.error to MaterialTheme.colorScheme.onError),
    "ErrorContainer" to (MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.onErrorContainer),
    "Background" to (MaterialTheme.colorScheme.background to MaterialTheme.colorScheme.onBackground),
    "Surface" to (MaterialTheme.colorScheme.surface to MaterialTheme.colorScheme.onSurface),
    "SurfaceVariant" to (MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant),
)

@Composable
internal fun ColorBox(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    val textColor = if (color.luminance() < 0.5f) Color.White else Color.Black
    Box(
        modifier = modifier
            .background(color)
    ) {
        Text(
            text = text,
            color = animateColorAsState(targetValue = textColor).value,
            modifier = Modifier.padding(8.dp),
        )
    }
}