package com.kmpalette.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import com.kmpalette.PaletteResult
import com.kmpalette.data.PresetImage
import com.kmpalette.data.presetImages
import com.kmpalette.extensions.file.rememberPlatformFilePaletteState
import com.kmpalette.extensions.resource.rememberResourcePaletteState
import com.kmpalette.palette.graphics.Palette
import com.kmpalette.ui.components.DominantColorCard
import com.kmpalette.ui.components.ImagePreview
import com.kmpalette.ui.components.ImageSelector
import com.kmpalette.ui.components.PaletteDisplay
import com.kmpalette.ui.components.ThemePreviewSection
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Main palette screen that displays image selection and generated palette.
 * Adapts layout based on window size - single column on compact, two-pane on expanded.
 */
@Composable
fun PaletteScreen(
    onDominantColorChanged: (Color?) -> Unit,
    modifier: Modifier = Modifier,
    scope: CoroutineScope = rememberCoroutineScope(),
) {
    var selectedPresetIndex by remember { mutableStateOf(0) }
    var customImageFile by remember { mutableStateOf<PlatformFile?>(null) }
    var customImageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var selectedSeedColor by remember { mutableStateOf<Color?>(null) }

    val resourcePaletteState = rememberResourcePaletteState()
    val filePaletteState = rememberPlatformFilePaletteState()

    val currentPalette = if (customImageFile != null) {
        filePaletteState.palette
    } else {
        resourcePaletteState.palette
    }

    val isLoading = if (customImageFile != null) {
        filePaletteState.state == PaletteResult.Loading
    } else {
        resourcePaletteState.state == PaletteResult.Loading
    }

    val dominantColor = currentPalette?.dominantSwatch?.let { Color(it.rgb) }
    val dominantOnColor = currentPalette?.dominantSwatch?.let { Color(it.bodyTextColor) }

    LaunchedEffect(dominantColor) {
        onDominantColorChanged(dominantColor)
    }

    LaunchedEffect(currentPalette) {
        if (selectedSeedColor == null || currentPalette != null) {
            selectedSeedColor = currentPalette?.dominantSwatch?.let { Color(it.rgb) }
        }
    }

    LaunchedEffect(selectedPresetIndex, customImageFile) {
        if (customImageFile != null) {
            filePaletteState.generate(customImageFile!!)
        } else {
            resourcePaletteState.generate(presetImages[selectedPresetIndex].resource)
        }
    }

    val filePicker = rememberFilePickerLauncher(
        type = FileKitType.Image,
        mode = FileKitMode.Single,
    ) { file ->
        if (file != null) {
            customImageFile = file
            scope.launch {
                try {
                    customImageBitmap = decodeImageBitmapFromFile(file)
                } catch (cause: Exception) {
                    if (cause is CancellationException) throw cause
                    // TODO: Use a snackbar to display the error
                    customImageBitmap = null
                }
            }
        }
    }

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val isExpandedWidth = windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)
    if (isExpandedWidth) {
        TwoPaneLayout(
            presets = presetImages,
            selectedPresetIndex = selectedPresetIndex,
            customImageBitmap = customImageBitmap,
            currentPalette = currentPalette,
            dominantColor = dominantColor,
            dominantOnColor = dominantOnColor,
            isLoading = isLoading,
            onPresetSelected = { index ->
                selectedPresetIndex = index
                customImageFile = null
                customImageBitmap = null
            },
            onCustomImageClick = { filePicker.launch() },
            selectedSeedColor = selectedSeedColor,
            onSeedColorSelected = { selectedSeedColor = it },
            modifier = modifier,
        )
    } else {
        SingleColumnLayout(
            presets = presetImages,
            selectedPresetIndex = selectedPresetIndex,
            customImageBitmap = customImageBitmap,
            currentPalette = currentPalette,
            dominantColor = dominantColor,
            dominantOnColor = dominantOnColor,
            isLoading = isLoading,
            onPresetSelected = { index ->
                selectedPresetIndex = index
                customImageFile = null
                customImageBitmap = null
            },
            onCustomImageClick = { filePicker.launch() },
            modifier = modifier,
        )
    }
}

@Composable
private fun SingleColumnLayout(
    presets: List<PresetImage>,
    selectedPresetIndex: Int,
    customImageBitmap: ImageBitmap?,
    currentPalette: Palette?,
    dominantColor: Color?,
    dominantOnColor: Color?,
    isLoading: Boolean,
    onPresetSelected: (Int) -> Unit,
    onCustomImageClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        ImagePreview(
            presetImage = if (customImageBitmap == null) presets.getOrNull(selectedPresetIndex) else null,
            customImage = customImageBitmap,
            isLoading = isLoading,
        )

        ImageSelector(
            presets = presets,
            selectedIndex = selectedPresetIndex,
            customImage = customImageBitmap,
            onPresetSelected = onPresetSelected,
            onCustomImageClick = onCustomImageClick,
        )

        DominantColorCard(
            color = dominantColor,
            onColor = dominantOnColor,
        )

        PaletteDisplay(
            palette = currentPalette,
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun TwoPaneLayout(
    presets: List<PresetImage>,
    selectedPresetIndex: Int,
    customImageBitmap: ImageBitmap?,
    currentPalette: Palette?,
    dominantColor: Color?,
    dominantOnColor: Color?,
    isLoading: Boolean,
    onPresetSelected: (Int) -> Unit,
    onCustomImageClick: () -> Unit,
    selectedSeedColor: Color?,
    onSeedColorSelected: (Color) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Column(
            modifier = Modifier
                .weight(0.45f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            ImagePreview(
                presetImage = if (customImageBitmap == null) presets.getOrNull(selectedPresetIndex) else null,
                customImage = customImageBitmap,
                isLoading = isLoading,
            )

            ImageSelector(
                presets = presets,
                selectedIndex = selectedPresetIndex,
                customImage = customImageBitmap,
                onPresetSelected = onPresetSelected,
                onCustomImageClick = onCustomImageClick,
            )
        }

        Column(
            modifier = Modifier
                .weight(0.55f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            DominantColorCard(
                color = dominantColor,
                onColor = dominantOnColor,
            )

            PaletteDisplay(
                palette = currentPalette,
                isHorizontal = true,
                selectedColor = selectedSeedColor,
                onColorSelected = onSeedColorSelected,
            )

            selectedSeedColor?.let { seed ->
                ThemePreviewSection(
                    seedColor = seed,
                )
            }
        }
    }
}

private suspend fun decodeImageBitmapFromFile(file: PlatformFile): ImageBitmap? =
    try {
        file.readBytes().decodeToImageBitmap()
    } catch (e: Exception) {
        null
    }
