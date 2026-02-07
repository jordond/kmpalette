package com.kmpalette.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * A prominent card displaying the dominant color extracted from an image.
 * Shows a large color preview, the hex code, and sample text demonstrating contrast.
 */
@Composable
fun DominantColorCard(
    color: Color?,
    onColor: Color?,
    modifier: Modifier = Modifier,
) {
    val animatedColor by animateColorAsState(
        targetValue = color ?: MaterialTheme.colorScheme.primary,
        animationSpec = tween(durationMillis = 300),
        label = "dominantColor",
    )

    val animatedOnColor by animateColorAsState(
        targetValue = onColor ?: MaterialTheme.colorScheme.onPrimary,
        animationSpec = tween(durationMillis = 300),
        label = "dominantOnColor",
    )

    val hexCode = color?.toHexString() ?: "N/A"
    val contrastTextColor = if (animatedColor.luminance() > 0.5f) Color.Black else Color.White

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 4.dp,
        shadowElevation = 4.dp,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            // Color preview area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(animatedColor),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "Dominant Color",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = contrastTextColor,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = hexCode,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = contrastTextColor,
                    )
                }
            }

            // Contrast demonstration
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        text = "On Color",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = onColor?.toHexString() ?: "N/A",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                    )
                }

                // Color swatches showing the pair
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(animatedColor),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "Aa",
                            style = MaterialTheme.typography.labelMedium,
                            color = animatedOnColor,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(animatedOnColor),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "Aa",
                            style = MaterialTheme.typography.labelMedium,
                            color = animatedColor,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
    }
}

/**
 * Converts a Color to its hex string representation.
 */
private fun Color.toHexString(): String {
    val red = (this.red * 255).toInt()
    val green = (this.green * 255).toInt()
    val blue = (this.blue * 255).toInt()
    return "#${red.toHexComponent()}${green.toHexComponent()}${blue.toHexComponent()}"
}

private fun Int.toHexComponent(): String = this.toString(16).padStart(2, '0').uppercase()
