package com.kmpalette.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

enum class SnackbarType {
    Success,
    Error,
    Info,
    Copied,
}

fun parseSnackbarType(message: String): SnackbarType =
    when {
        message.startsWith("Copied") -> SnackbarType.Copied
        message.startsWith("Failed") || message.startsWith("Error") -> SnackbarType.Error
        else -> SnackbarType.Info
    }

@Composable
fun AppSnackbar(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier,
) {
    val message = snackbarData.visuals.message
    val type = parseSnackbarType(message)

    val containerColor by animateColorAsState(
        targetValue = when (type) {
            SnackbarType.Success -> MaterialTheme.colorScheme.primaryContainer
            SnackbarType.Error -> MaterialTheme.colorScheme.errorContainer
            SnackbarType.Info -> MaterialTheme.colorScheme.secondaryContainer
            SnackbarType.Copied -> MaterialTheme.colorScheme.tertiaryContainer
        },
        label = "snackbarContainerColor",
    )

    val contentColor by animateColorAsState(
        targetValue = when (type) {
            SnackbarType.Success -> MaterialTheme.colorScheme.onPrimaryContainer
            SnackbarType.Error -> MaterialTheme.colorScheme.onErrorContainer
            SnackbarType.Info -> MaterialTheme.colorScheme.onSecondaryContainer
            SnackbarType.Copied -> MaterialTheme.colorScheme.onTertiaryContainer
        },
        label = "snackbarContentColor",
    )

    val icon: ImageVector = when (type) {
        SnackbarType.Success -> Icons.Rounded.CheckCircle
        SnackbarType.Error -> Icons.Rounded.Error
        SnackbarType.Info -> Icons.Rounded.Info
        SnackbarType.Copied -> Icons.Rounded.ContentCopy
    }

    Snackbar(
        modifier = modifier.padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        containerColor = containerColor,
        contentColor = contentColor,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 12.dp),
            )
        }
    }
}
