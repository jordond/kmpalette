package com.kmpalette.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kmpalette.palette.graphics.Palette

/**
 * Displays all 6 swatches from a generated palette in a 2x3 grid.
 * Shows: Vibrant, Light Vibrant, Muted, Light Muted, Dark Vibrant, Dark Muted
 */
@Composable
fun PaletteDisplay(
    palette: Palette?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Color Palette",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SwatchCard(
                color = palette?.vibrantSwatch?.let {
                    androidx.compose.ui.graphics
                        .Color(it.rgb)
                },
                label = "Vibrant",
                modifier = Modifier.weight(1f),
            )
            SwatchCard(
                color = palette?.lightVibrantSwatch?.let {
                    androidx.compose.ui.graphics
                        .Color(it.rgb)
                },
                label = "Light Vibrant",
                modifier = Modifier.weight(1f),
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SwatchCard(
                color = palette?.mutedSwatch?.let {
                    androidx.compose.ui.graphics
                        .Color(it.rgb)
                },
                label = "Muted",
                modifier = Modifier.weight(1f),
            )
            SwatchCard(
                color = palette?.lightMutedSwatch?.let {
                    androidx.compose.ui.graphics
                        .Color(it.rgb)
                },
                label = "Light Muted",
                modifier = Modifier.weight(1f),
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SwatchCard(
                color = palette?.darkVibrantSwatch?.let {
                    androidx.compose.ui.graphics
                        .Color(it.rgb)
                },
                label = "Dark Vibrant",
                modifier = Modifier.weight(1f),
            )
            SwatchCard(
                color = palette?.darkMutedSwatch?.let {
                    androidx.compose.ui.graphics
                        .Color(it.rgb)
                },
                label = "Dark Muted",
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        val swatchCount = remember(palette) {
            listOfNotNull(
                palette?.vibrantSwatch,
                palette?.lightVibrantSwatch,
                palette?.mutedSwatch,
                palette?.lightMutedSwatch,
                palette?.darkVibrantSwatch,
                palette?.darkMutedSwatch,
            ).size
        }

        Text(
            text = "$swatchCount of 6 swatches extracted",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
