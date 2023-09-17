package com.kmpalette.demo.dominant

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.kmpalette.DominantColorState
import com.kmpalette.PaletteResult
import com.kmpalette.demo.theme.AppTheme
import com.kmpalette.demo.util.ColorBox
import com.kmpalette.demo.util.colorSchemePairs
import com.kmpalette.rememberDominantColorState
import com.materialkolor.PaletteStyle
import com.mohamedrejeb.calf.io.readByteArray
import com.mohamedrejeb.calf.picker.FilePickerFileType
import com.mohamedrejeb.calf.picker.FilePickerSelectionMode
import com.mohamedrejeb.calf.picker.rememberFilePickerLauncher
import com.mohamedrejeb.calf.picker.toImageBitmap

class DominantPhotoColorScreen : Screen {

    @Composable
    override fun Content() {
        val dominantColorState = rememberDominantColorState {
            clearFilters()
        }
        var selectedPhoto: ImageBitmap? by remember { mutableStateOf(null) }
        val pickerLauncher = rememberFilePickerLauncher(
            type = FilePickerFileType.Image,
            selectionMode = FilePickerSelectionMode.Single,
            onResult = { files ->
                val bitmap = files.firstOrNull()?.readByteArray()?.toImageBitmap()
                selectedPhoto = bitmap
            }
        )

        LaunchedEffect(selectedPhoto) {
            if (selectedPhoto != null) {
                dominantColorState.updateFrom(selectedPhoto!!)
            }
        }

        DominantDemoContent(
            dominantColorState = dominantColorState,
            imageBitmap = selectedPhoto,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Button(onClick = { pickerLauncher.launch() }) {
                    Text("Choose a photo")
                }
            }
        }
    }
}

@Composable
internal fun <T : Any> DominantDemoContent(
    dominantColorState: DominantColorState<T>,
    imageBitmap: ImageBitmap?,
    content: @Composable () -> Unit,
) {
    var style: PaletteStyle by remember { mutableStateOf(PaletteStyle.TonalSpot) }

    AppTheme(
        seedColor = dominantColorState.color,
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
            content()

            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap,
                    contentDescription = null,
                    modifier = Modifier.heightIn(max = 200.dp)
                )
            }

            when (dominantColorState.result) {
                is PaletteResult.Error -> {
                    val error = (dominantColorState.result as PaletteResult.Error).cause
                    Text("Something went wrong: $error")
                }
                is PaletteResult.Loading -> {
                    CircularProgressIndicator()
                    Text("Loading...")
                }
                is PaletteResult.Success -> {
                    SuccessView(dominantColorState = dominantColorState)
                    FlowRow(
                        modifier = Modifier
                            .padding(2.dp)
                            .fillMaxWidth()
                    ) {
                        PaletteStyle.entries.forEach { paletteStyle ->
                            FilterChip(
                                label = { Text(text = paletteStyle.name) },
                                selected = style == paletteStyle,
                                onClick = { style = paletteStyle },
                            )
                        }
                    }
                }
                null -> {}
            }
        }
    }
}

@Composable
private fun SuccessView(
    dominantColorState: DominantColorState<*>,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text("Dominant color: 0x${dominantColorState.color.toArgb().toHexString().uppercase()}")
        Box(
            modifier = Modifier
                .size(64.dp)
                .padding(8.dp)
                .background(dominantColorState.color),
        )
    }

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