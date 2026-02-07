package com.kmpalette.data

import androidx.compose.runtime.Immutable
import kmpalette.app.shared.generated.resources.Res
import kmpalette.app.shared.generated.resources.img_preset_1
import org.jetbrains.compose.resources.DrawableResource

/**
 * Data class representing a preset image for palette generation.
 */
@Immutable
data class PresetImage(
    val resource: DrawableResource,
    val name: String,
)

/**
 * List of preset images included in the app.
 * Users can select from these to generate palettes, or pick their own image.
 */
val presetImages: List<PresetImage> = listOf(
    PresetImage(Res.drawable.img_preset_1, "Coastal Sunset"),
    PresetImage(Res.drawable.img_preset_1, "Mountain Dawn"),
    PresetImage(Res.drawable.img_preset_1, "Forest Lake"),
    PresetImage(Res.drawable.img_preset_1, "Desert Dunes"),
    PresetImage(Res.drawable.img_preset_1, "Ocean Waves"),
    PresetImage(Res.drawable.img_preset_1, "Autumn Leaves"),
    PresetImage(Res.drawable.img_preset_1, "City Lights"),
    PresetImage(Res.drawable.img_preset_1, "Northern Lights"),
)
