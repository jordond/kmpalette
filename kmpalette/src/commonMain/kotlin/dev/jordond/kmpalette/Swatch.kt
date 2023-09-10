package dev.jordond.kmpalette

import androidx.compose.ui.graphics.Color
import dev.jordond.kmpalette.palette.graphics.Palette
import dev.jordond.kmpalette.palette.graphics.Target

public sealed interface SwatchTarget {

    public data object Vibrant : SwatchTarget
    public data object VibrantDark : SwatchTarget
    public data object VibrantLight : SwatchTarget
    public data object Muted : SwatchTarget
    public data object MutedDark : SwatchTarget
    public data object MutedLight : SwatchTarget
}

/**
 * Returns the selected swatch for the given target from the palette, or `null` if one
 * could not be found.
 *
 * @see Palette.getSwatchForTarget
 */
public inline operator fun Palette.get(target: Target): Palette.Swatch? = getSwatchForTarget(target)

public inline operator fun Palette.get(target: SwatchTarget): Palette.Swatch? = getSwatch(target)

public fun Palette.getSwatch(target: SwatchTarget): Palette.Swatch? {
    return when (target) {
        SwatchTarget.Vibrant -> vibrantSwatch
        SwatchTarget.VibrantDark -> darkVibrantSwatch
        SwatchTarget.VibrantLight -> lightVibrantSwatch
        SwatchTarget.Muted -> mutedSwatch
        SwatchTarget.MutedDark -> darkMutedSwatch
        SwatchTarget.MutedLight -> lightMutedSwatch
    }
}

public fun Palette.Swatch.titleTextColor(): Color {
    return Color(titleTextColor)
}

public fun Palette.Swatch.bodyTextColor(): Color {
    return Color(bodyTextColor)
}