package com.kmpalette.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kmpalette.palette.graphics.Palette

/**
 * Displays swatches from a generated palette.
 * Supports grid mode (2x3) and horizontal strip mode.
 *
 * @param palette The generated palette object.
 * @param modifier Modifier for the layout.
 * @param isHorizontal Whether to display in a horizontal strip (desktop) or grid (mobile).
 * @param selectedColor The currently selected color, if any.
 * @param onColorSelected Callback when a color is selected.
 */
@Composable
fun PaletteDisplay(
    palette: Palette?,
    modifier: Modifier = Modifier,
    isHorizontal: Boolean = false,
    selectedColor: Color? = null,
    onColorSelected: ((Color) -> Unit)? = null,
) {
    if (isHorizontal) {
        val swatches = remember(palette) {
            listOfNotNull(
                palette?.dominantSwatch?.let { SwatchEntry("Dominant", Color(it.rgb)) },
                palette?.vibrantSwatch?.let { SwatchEntry("Vibrant", Color(it.rgb)) },
                palette?.lightVibrantSwatch?.let { SwatchEntry("Light Vibrant", Color(it.rgb)) },
                palette?.mutedSwatch?.let { SwatchEntry("Muted", Color(it.rgb)) },
                palette?.lightMutedSwatch?.let { SwatchEntry("Light Muted", Color(it.rgb)) },
                palette?.darkVibrantSwatch?.let { SwatchEntry("Dark Vibrant", Color(it.rgb)) },
                palette?.darkMutedSwatch?.let { SwatchEntry("Dark Muted", Color(it.rgb)) },
            )
        }

        LazyRow(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(swatches) { entry ->
                SwatchCard(
                    color = entry.color,
                    label = entry.label,
                    modifier = Modifier
                        .width(100.dp)
                        .height(80.dp),
                    isSelected = entry.color == selectedColor,
                    onClick = { entry.color?.let { onColorSelected?.invoke(it) } },
                )
            }
        }
    } else {
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
                    color = palette?.vibrantSwatch?.let { Color(it.rgb) },
                    label = "Vibrant",
                    modifier = Modifier.weight(1f).aspectRatio(1f),
                    isSelected = palette?.vibrantSwatch?.let { Color(it.rgb) } == selectedColor,
                    onClick = onColorSelected?.let { cb -> { palette?.vibrantSwatch?.let { cb(Color(it.rgb)) } } },
                )
                SwatchCard(
                    color = palette?.lightVibrantSwatch?.let { Color(it.rgb) },
                    label = "Light Vibrant",
                    modifier = Modifier.weight(1f).aspectRatio(1f),
                    isSelected = palette?.lightVibrantSwatch?.let { Color(it.rgb) } == selectedColor,
                    onClick = onColorSelected?.let { cb -> { palette?.lightVibrantSwatch?.let { cb(Color(it.rgb)) } } },
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                SwatchCard(
                    color = palette?.mutedSwatch?.let { Color(it.rgb) },
                    label = "Muted",
                    modifier = Modifier.weight(1f).aspectRatio(1f),
                    isSelected = palette?.mutedSwatch?.let { Color(it.rgb) } == selectedColor,
                    onClick = onColorSelected?.let { cb -> { palette?.mutedSwatch?.let { cb(Color(it.rgb)) } } },
                )
                SwatchCard(
                    color = palette?.lightMutedSwatch?.let { Color(it.rgb) },
                    label = "Light Muted",
                    modifier = Modifier.weight(1f).aspectRatio(1f),
                    isSelected = palette?.lightMutedSwatch?.let { Color(it.rgb) } == selectedColor,
                    onClick = onColorSelected?.let { cb -> { palette?.lightMutedSwatch?.let { cb(Color(it.rgb)) } } },
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                SwatchCard(
                    color = palette?.darkVibrantSwatch?.let { Color(it.rgb) },
                    label = "Dark Vibrant",
                    modifier = Modifier.weight(1f).aspectRatio(1f),
                    isSelected = palette?.darkVibrantSwatch?.let { Color(it.rgb) } == selectedColor,
                    onClick = onColorSelected?.let { cb -> { palette?.darkVibrantSwatch?.let { cb(Color(it.rgb)) } } },
                )
                SwatchCard(
                    color = palette?.darkMutedSwatch?.let { Color(it.rgb) },
                    label = "Dark Muted",
                    modifier = Modifier.weight(1f).aspectRatio(1f),
                    isSelected = palette?.darkMutedSwatch?.let { Color(it.rgb) } == selectedColor,
                    onClick = onColorSelected?.let { cb -> { palette?.darkMutedSwatch?.let { cb(Color(it.rgb)) } } },
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
}

private data class SwatchEntry(
    val label: String,
    val color: Color?,
)
