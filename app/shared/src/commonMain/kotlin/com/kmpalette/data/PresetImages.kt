package com.kmpalette.data

import androidx.compose.runtime.Immutable
import kmpalette.app.shared.generated.resources.Res
import kmpalette.app.shared.generated.resources.img_preset_1
import kmpalette.app.shared.generated.resources.img_preset_10
import kmpalette.app.shared.generated.resources.img_preset_2
import kmpalette.app.shared.generated.resources.img_preset_3
import kmpalette.app.shared.generated.resources.img_preset_4
import kmpalette.app.shared.generated.resources.img_preset_5
import kmpalette.app.shared.generated.resources.img_preset_6
import kmpalette.app.shared.generated.resources.img_preset_7
import kmpalette.app.shared.generated.resources.img_preset_8
import kmpalette.app.shared.generated.resources.img_preset_9
import org.jetbrains.compose.resources.DrawableResource

@Immutable
data class PresetImage(
    val resource: DrawableResource,
)

val presetImages: List<PresetImage> = listOf(
    PresetImage(Res.drawable.img_preset_1),
    PresetImage(Res.drawable.img_preset_2),
    PresetImage(Res.drawable.img_preset_3),
    PresetImage(Res.drawable.img_preset_4),
    PresetImage(Res.drawable.img_preset_5),
    PresetImage(Res.drawable.img_preset_6),
    PresetImage(Res.drawable.img_preset_7),
    PresetImage(Res.drawable.img_preset_8),
    PresetImage(Res.drawable.img_preset_9),
    PresetImage(Res.drawable.img_preset_10),
)
