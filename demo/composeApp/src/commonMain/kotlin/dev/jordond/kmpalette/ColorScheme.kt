package dev.jordond.kmpalette

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

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