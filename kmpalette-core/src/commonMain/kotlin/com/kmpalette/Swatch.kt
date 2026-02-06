package com.kmpalette

import androidx.compose.ui.graphics.Color
import com.kmpalette.palette.graphics.Palette
import com.kmpalette.palette.graphics.Palette.Builder
import com.kmpalette.palette.graphics.Palette.Swatch
import com.kmpalette.palette.graphics.Target

/**
 * A class which allows custom selection of colors in a [Palette]'s generation. Instances
 * can be created via the [Builder] class.
 *
 * Mapped to [Target] in `androidx-palette`.
 */
public sealed interface SwatchTarget {
    /**
     * Target for vibrant colors with high saturation.
     */
    public data object Vibrant : SwatchTarget

    /**
     * Target for dark vibrant colors with high saturation and low lightness.
     */
    public data object VibrantDark : SwatchTarget

    /**
     * Target for light vibrant colors with high saturation and high lightness.
     */
    public data object VibrantLight : SwatchTarget

    /**
     * Target for muted colors with low saturation.
     */
    public data object Muted : SwatchTarget

    /**
     * Target for dark muted colors with low saturation and low lightness.
     */
    public data object MutedDark : SwatchTarget

    /**
     * Target for light muted colors with low saturation and high lightness.
     */
    public data object MutedLight : SwatchTarget
}

/**
 * Returns the selected swatch for the given target from the palette, or `null` if one
 * could not be found.
 *
 * This operator function allows indexed access to palette swatches using bracket notation.
 *
 * @see Palette.getSwatchForTarget
 * @param[target] The [Target] selection.
 * @return The selected [Swatch] for the given target from the palette, or `null` if not found.
 */
public operator fun Palette.get(target: Target): Swatch? = getSwatchForTarget(target)

/**
 * Returns the selected swatch for the given target from the palette, or `null` if one
 * could not be found.
 *
 * @see Palette.get
 * @param[target] The target selection.
 * @return The selected swatch for the given target from the palette, or `null`.
 */
public operator fun Palette.get(target: SwatchTarget): Swatch? = getSwatch(target)

/**
 * Returns the selected swatch for the given target from the palette, or `null` if one
 * could not be found.
 *
 * @see Palette.get
 * @param[target] The target selection.
 * @return The selected swatch for the given target from the palette, or `null`.
 */
public fun Palette.getSwatch(target: SwatchTarget): Swatch? =
    when (target) {
        SwatchTarget.Vibrant -> vibrantSwatch
        SwatchTarget.VibrantDark -> darkVibrantSwatch
        SwatchTarget.VibrantLight -> lightVibrantSwatch
        SwatchTarget.Muted -> mutedSwatch
        SwatchTarget.MutedDark -> darkMutedSwatch
        SwatchTarget.MutedLight -> lightMutedSwatch
    }

/**
 * Returns a color value in the RGB color space which represents the color of this [Swatch]
 *
 * @see Swatch.rgb
 */
public val Swatch.color: Color
    get() = Color(rgb)

/**
 * Returns an appropriate color to use for any 'title' text which is displayed over this
 * [Swatch]'s color. This color is guaranteed to have sufficient contrast.
 *
 * @see Swatch.titleTextColor
 * @return A [Color] to be used for text over the [Swatch]'s color.
 */
public val Swatch.onColor: Color
    get() = Color(titleTextColor)

/**
 * Returns an appropriate color to use for any 'title' text which is displayed over this
 * [Swatch]'s color. This color is guaranteed to have sufficient contrast.
 *
 * @see Swatch.titleTextColor
 * @return A [Color] to be used for text over the [Swatch]'s color.
 */
public fun Swatch.titleTextColor(): Color = Color(titleTextColor)

/**
 * Returns an appropriate color to use for any 'body' text which is displayed over this
 * [Swatch]'s color. This color is guaranteed to have sufficient contrast.
 *
 * @see Swatch.bodyTextColor
 * @return A [Color] to be used for text over the [Swatch]'s color.
 */
public fun Swatch.bodyTextColor(): Color = Color(bodyTextColor)
