@file:OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)

package com.kmpalette.demo.palette

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.kmpalette.PaletteResult
import com.kmpalette.PaletteState
import com.kmpalette.demo.SelectedColor
import com.kmpalette.demo.defaultColor
import com.kmpalette.demo.theme.AppTheme
import com.kmpalette.demo.util.ColorBox
import com.kmpalette.demo.util.colorSchemePairs
import com.kmpalette.demo.util.conditional
import com.kmpalette.palette.graphics.Palette
import com.materialkolor.PaletteStyle

@Composable
fun <T : Any> PaletteScreen(
    images: List<T>,
    paletteState: PaletteState<T>,
    painterResource: @Composable (index: Int, item: T) -> Painter,
) {
    var selected: SelectedColor by remember { mutableStateOf(defaultColor) }
    var style: PaletteStyle by remember { mutableStateOf(PaletteStyle.TonalSpot) }

    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    val selectedImage: T? by remember(selectedIndex) {
        derivedStateOf {
            if (selectedIndex == null) null
            else images[selectedIndex!!]
        }
    }

    LaunchedEffect(selectedImage) {
        selectedImage?.let { paletteState.generate(it) }
    }

    AppTheme(
        seedColor = selected.color,
        paletteStyle = style,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(8.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth().wrapContentWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalArrangement = Arrangement.spacedBy(4.dp),
                maxItemsInEachRow = 5,
            ) {
                images.forEachIndexed { index, image ->
                    Image(
                        painter = painterResource(index, image),
                        contentDescription = null,
                        modifier = Modifier
                            .size(64.dp)
                            .clickable {
                                selectedIndex = if (selectedIndex == index) null else index
                                selected = defaultColor
                            }
                            .conditional(
                                condition = index == selectedIndex,
                                block = {
                                    Modifier.border(2.dp, MaterialTheme.colorScheme.primary)
                                },
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (paletteState.state == null) {
                Text("Select an image to generate a palette")
            } else if (paletteState.state is PaletteResult.Error) {
                Text("Error generating palette: ${(paletteState.state as PaletteResult.Error).cause}")
            } else if (paletteState.palette != null) {
                val palette = paletteState.palette
                val onClick = { newColor: SelectedColor ->
                    selected = if (newColor == selected) defaultColor else newColor
                }

                Row {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(2.dp)
                            .fillMaxWidth()
                            .weight(1f)
                            .background(MaterialTheme.colorScheme.secondaryContainer),
                    ) {
                        SwatchRow(palette, "Dominant", selected, onClick) { it.dominantSwatch }
                        SwatchRow(palette, "Vibrant", selected, onClick) { it.vibrantSwatch }
                        SwatchRow(palette, "Light Vibrant", selected, onClick) { it.lightVibrantSwatch }
                        SwatchRow(palette, "Dark Vibrant", selected, onClick) { it.darkVibrantSwatch }
                        SwatchRow(palette, "Muted", selected, onClick) { it.mutedSwatch }
                        SwatchRow(palette, "Light Muted", selected, onClick) { it.lightMutedSwatch }
                        SwatchRow(palette, "Dark Muted", selected, onClick) { it.darkMutedSwatch }
                    }

                    FlowRow(
                        modifier = Modifier
                            .padding(2.dp)
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        if (selected != defaultColor) {
                            PaletteStyle.entries.forEach { paletteStyle ->
                                FilterChip(
                                    label = { Text(text = paletteStyle.name) },
                                    selected = style == paletteStyle,
                                    onClick = { style = paletteStyle },
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (selected != defaultColor) {
                    Column {
                        colorSchemePairs().forEach { (name, colors) ->
                            val (color, onColor) = colors

                            Row(modifier = Modifier.fillMaxWidth()) {
                                ColorBox(text = name, color = color, modifier = Modifier.weight(1f))
                                ColorBox(text = "On$name", color = onColor, modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SwatchRow(
    palette: Palette?,
    name: String,
    selectedColor: SelectedColor,
    onClick: (SelectedColor) -> Unit,
    select: (Palette) -> Palette.Swatch?,
) {
    val color by remember(palette) {
        derivedStateOf {
            if (palette == null) null
            else select(palette)?.rgb?.let { Color(it) }
        }
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .conditional(
                condition = color != null,
                block = {
                    Modifier.clickable { color?.let { onClick(SelectedColor(name, it)) } }
                },
            )
            .conditional(
                condition = selectedColor.name == name && selectedColor.color == color,
                block = {
                    Modifier.border(2.dp, MaterialTheme.colorScheme.primary)
                },
            ),
    ) {
        Text("$name: ")
        Box(
            Modifier
                .conditional(color != null) { Modifier.size(24.dp) }
                .background(color ?: Color.Transparent)
        ) {
            if (color == null) {
                Text("N/A")
            }
        }
    }
}