/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kmpalette.palette.graphics

import androidx.annotation.ColorInt
import com.kmpalette.palette.internal.ColorCutQuantizer
import com.kmpalette.palette.internal.Region
import com.kmpalette.palette.internal.ScaledPixels
import com.kmpalette.palette.internal.scalePixelsToArea
import com.kmpalette.palette.internal.utils.ColorUtils
import dev.drewhamilton.poko.Poko
import kotlin.math.abs

/**
 * A helper class to extract prominent colors from an image.
 *
 * A number of colors with different profiles are extracted from the image:
 *
 *  * Vibrant
 *  * Vibrant Dark
 *  * Vibrant Light
 *  * Muted
 *  * Muted Dark
 *  * Muted Light
 *
 * These can be retrieved from the appropriate getter method.
 *
 * Instances are created with a [Builder] which supports several options to tweak the
 * generated Palette. See that class' documentation for more information.
 *
 *
 * Generation should always be completed on a background thread, ideally the one in
 * which you load your image on. [Builder] supports both synchronous and asynchronous
 * generation:
 *
 * ```
 * Palette p = Palette.from(pixels, width, height).generate();
 * ```
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
public class Palette internal constructor(
    public val swatches: List<Swatch>,
    private val targets: List<Target>,
) {
    private val selectedSwatches: HashMap<Target, Swatch?> = HashMap()
    private val usedColors: HashMap<Int, Boolean> = HashMap()

    public val dominantSwatch: Swatch? = findDominantSwatch()

    public val vibrantSwatch: Swatch?
        get() = getSwatchForTarget(Target.VIBRANT)

    public val lightVibrantSwatch: Swatch?
        get() = getSwatchForTarget(Target.LIGHT_VIBRANT)

    public val darkVibrantSwatch: Swatch?
        get() = getSwatchForTarget(Target.DARK_VIBRANT)

    public val mutedSwatch: Swatch?
        get() = getSwatchForTarget(Target.MUTED)

    public val lightMutedSwatch: Swatch?
        get() = getSwatchForTarget(Target.LIGHT_MUTED)

    public val darkMutedSwatch: Swatch?
        get() = getSwatchForTarget(Target.DARK_MUTED)

    @ColorInt
    public fun getVibrantColor(
        @ColorInt defaultColor: Int,
    ): Int = getColorForTarget(Target.VIBRANT, defaultColor)

    @ColorInt
    public fun getLightVibrantColor(
        @ColorInt defaultColor: Int,
    ): Int = getColorForTarget(Target.LIGHT_VIBRANT, defaultColor)

    @ColorInt
    public fun getDarkVibrantColor(
        @ColorInt defaultColor: Int,
    ): Int = getColorForTarget(Target.DARK_VIBRANT, defaultColor)

    @ColorInt
    public fun getMutedColor(
        @ColorInt defaultColor: Int,
    ): Int = getColorForTarget(Target.MUTED, defaultColor)

    @ColorInt
    public fun getLightMutedColor(
        @ColorInt defaultColor: Int,
    ): Int = getColorForTarget(Target.LIGHT_MUTED, defaultColor)

    @ColorInt
    public fun getDarkMutedColor(
        @ColorInt defaultColor: Int,
    ): Int = getColorForTarget(Target.DARK_MUTED, defaultColor)

    public fun getSwatchForTarget(target: Target): Swatch? = selectedSwatches[target]

    @ColorInt
    public fun getColorForTarget(
        target: Target,
        @ColorInt defaultColor: Int,
    ): Int {
        val swatch = getSwatchForTarget(target)
        return swatch?.rgb ?: defaultColor
    }

    @ColorInt
    public fun getDominantColor(
        @ColorInt defaultColor: Int,
    ): Int = dominantSwatch?.rgb ?: defaultColor

    internal fun generate() {
        targets.forEach { target ->
            target.normalizeWeights()
            selectedSwatches[target] = generateScoredTarget(target)
        }
        usedColors.clear()
    }

    private fun generateScoredTarget(target: Target): Swatch? {
        val maxScoreSwatch = getMaxScoredSwatchForTarget(target)
        if (maxScoreSwatch != null && target.isExclusive) {
            usedColors[maxScoreSwatch.rgb] = true
        }
        return maxScoreSwatch
    }

    private fun getMaxScoredSwatchForTarget(target: Target): Swatch? {
        var maxScore = 0f
        var maxScoreSwatch: Swatch? = null
        var i = 0
        val count = swatches.size
        while (i < count) {
            val swatch = swatches[i]
            if (shouldBeScoredForTarget(swatch, target)) {
                val score = generateScore(swatch, target)
                if (maxScoreSwatch == null || score > maxScore) {
                    maxScoreSwatch = swatch
                    maxScore = score
                }
            }
            i++
        }
        return maxScoreSwatch
    }

    private fun shouldBeScoredForTarget(
        swatch: Swatch,
        target: Target,
    ): Boolean {
        val hsl = swatch.hsl
        return hsl[1] >= target.minimumSaturation &&
                hsl[1] <= target.maximumSaturation &&
                hsl[2] >= target.minimumLightness &&
                hsl[2] <= target.maximumLightness &&
                usedColors[swatch.rgb]?.not() ?: true
    }

    private fun generateScore(
        swatch: Swatch,
        target: Target,
    ): Float {
        val hsl = swatch.hsl
        var saturationScore = 0f
        var luminanceScore = 0f
        var populationScore = 0f
        val maxPopulation = dominantSwatch?.population ?: 1
        if (target.saturationWeight > 0) {
            saturationScore = (
                    target.saturationWeight
                            * (1f - abs(hsl[1] - target.targetSaturation))
                    )
        }
        if (target.lightnessWeight > 0) {
            luminanceScore = (
                    target.lightnessWeight
                            * (1f - abs(hsl[2] - target.targetLightness))
                    )
        }
        if (target.populationWeight > 0) {
            populationScore = (
                    target.populationWeight
                            * (swatch.population / maxPopulation.toFloat())
                    )
        }
        return saturationScore + luminanceScore + populationScore
    }

    private fun findDominantSwatch(): Swatch? = swatches.maxByOrNull { it.population }

    @Poko
    public class Swatch(
        @get:ColorInt
        public val rgb: Int,
        public val population: Int,
    ) {
        private val red: Int = ColorUtils.red(rgb)
        private val green: Int = ColorUtils.green(rgb)
        private val blue: Int = ColorUtils.blue(rgb)

        private var generatedTextColors = false
        private var _titleTextColor = 0
        private var _bodyTextColor = 0

        public var hsl: FloatArray =
            FloatArray(3)
                .apply { ColorUtils.convertRGBToHSL(red, green, blue, this) }
            private set

        @get:ColorInt
        public val titleTextColor: Int
            get() {
                ensureTextColorsGenerated()
                return _titleTextColor
            }

        @get:ColorInt
        public val bodyTextColor: Int
            get() {
                ensureTextColorsGenerated()
                return _bodyTextColor
            }

        private fun ensureTextColorsGenerated() {
            if (!generatedTextColors) {
                val lightBodyAlpha: Int =
                    ColorUtils.calculateMinimumAlpha(
                        ColorUtils.WHITE,
                        rgb,
                        MIN_CONTRAST_BODY_TEXT,
                    )
                val lightTitleAlpha: Int =
                    ColorUtils.calculateMinimumAlpha(
                        ColorUtils.WHITE,
                        rgb,
                        MIN_CONTRAST_TITLE_TEXT,
                    )

                if (lightBodyAlpha != -1 && lightTitleAlpha != -1) {
                    _bodyTextColor = ColorUtils.setAlpha(ColorUtils.WHITE, lightBodyAlpha)
                    _titleTextColor = ColorUtils.setAlpha(ColorUtils.WHITE, lightTitleAlpha)
                    generatedTextColors = true
                    return
                }

                val darkBodyAlpha: Int =
                    ColorUtils.calculateMinimumAlpha(
                        ColorUtils.BLACK,
                        rgb,
                        MIN_CONTRAST_BODY_TEXT,
                    )
                val darkTitleAlpha: Int =
                    ColorUtils.calculateMinimumAlpha(
                        ColorUtils.BLACK,
                        rgb,
                        MIN_CONTRAST_TITLE_TEXT,
                    )
                if (darkBodyAlpha != -1 && darkTitleAlpha != -1) {
                    _bodyTextColor = ColorUtils.setAlpha(ColorUtils.BLACK, darkBodyAlpha)
                    _titleTextColor = ColorUtils.setAlpha(ColorUtils.BLACK, darkTitleAlpha)
                    generatedTextColors = true
                    return
                }

                _bodyTextColor =
                    if (lightBodyAlpha != -1) {
                        ColorUtils.setAlpha(ColorUtils.WHITE, lightBodyAlpha)
                    } else {
                        ColorUtils.setAlpha(ColorUtils.BLACK, darkBodyAlpha)
                    }

                _titleTextColor =
                    if (lightTitleAlpha != -1) {
                        ColorUtils.setAlpha(
                            ColorUtils.WHITE,
                            lightTitleAlpha,
                        )
                    } else {
                        ColorUtils.setAlpha(ColorUtils.BLACK, darkTitleAlpha)
                    }

                generatedTextColors = true
            }
        }
    }

    public class Builder {
        private val swatches: List<Swatch>?
        private val pixels: IntArray?
        private val width: Int
        private val height: Int

        private val targets: MutableList<Target> = mutableListOf()
        private var maxColors = DEFAULT_CALCULATE_NUMBER_COLORS
        private var resizeArea = DEFAULT_RESIZE_BITMAP_AREA
        private val filters: MutableList<Filter> = mutableListOf()
        private var region: Region? = null

        public constructor(pixels: IntArray, width: Int, height: Int) {
            require(pixels.size == width * height) {
                "Pixel array size (${pixels.size}) must equal width * height ($width * $height = ${width * height})"
            }
            filters.add(DEFAULT_FILTER)
            this.pixels = pixels
            this.width = width
            this.height = height
            swatches = null

            targets.add(Target.LIGHT_VIBRANT)
            targets.add(Target.VIBRANT)
            targets.add(Target.DARK_VIBRANT)
            targets.add(Target.LIGHT_MUTED)
            targets.add(Target.MUTED)
            targets.add(Target.DARK_MUTED)
        }

        public constructor(swatches: List<Swatch>) {
            if (swatches.isEmpty()) {
                throw IllegalArgumentException("List of Swatches is not valid")
            }
            filters.add(DEFAULT_FILTER)
            this.swatches = swatches
            pixels = null
            width = 0
            height = 0
        }

        public fun maximumColorCount(colors: Int): Builder {
            maxColors = colors
            return this
        }

        public fun resizeBitmapArea(area: Int): Builder {
            resizeArea = area
            return this
        }

        public fun clearFilters(): Builder {
            filters.clear()
            return this
        }

        public fun addFilter(filter: Filter): Builder {
            filters.add(filter)
            return this
        }

        public fun setRegion(
            left: Int,
            top: Int,
            right: Int,
            bottom: Int,
        ): Builder {
            if (pixels != null) {
                val bitmapRegion = Region(0, 0, width, height)
                val requestedRegion = Region(left, top, right, bottom)
                if (!bitmapRegion.overlaps(requestedRegion)) {
                    throw IllegalArgumentException(
                        "The given region must intersect with the image dimensions.",
                    )
                }
                region = requestedRegion.intersect(bitmapRegion)
            }
            return this
        }

        public fun clearRegion(): Builder {
            region = null
            return this
        }

        public fun addTarget(target: Target): Builder {
            if (!targets.contains(target)) {
                targets.add(target)
            }
            return this
        }

        public fun clearTargets(): Builder {
            targets.clear()
            return this
        }

        public fun generate(): Palette {
            val swatches: List<Swatch>
            if (pixels != null) {
                val scaled: ScaledPixels = scalePixelsToArea(pixels, width, height, resizeArea)
                var currentRegion = region
                if (scaled.width != width && currentRegion != null) {
                    val scale = scaled.width.toFloat() / width
                    currentRegion = currentRegion.scale(scale).coerceIn(scaled.width, scaled.height)
                }

                val pixelsToQuantize =
                    getPixelsFromRegion(
                        scaled.pixels,
                        scaled.width,
                        scaled.height,
                        currentRegion,
                    )

                val quantizer =
                    ColorCutQuantizer(
                        pixels = pixelsToQuantize,
                        maxColors = maxColors,
                        filters = if (filters.isEmpty()) null else filters.toTypedArray(),
                    )
                swatches = quantizer.quantizedColors
            } else if (this.swatches != null) {
                swatches = this.swatches
            } else {
                throw AssertionError()
            }

            val p = Palette(swatches, targets)
            p.generate()
            return p
        }

        private fun getPixelsFromRegion(
            pixels: IntArray,
            width: Int,
            height: Int,
            region: Region?,
        ): IntArray {
            if (region == null) {
                return pixels
            }

            val regionWidth = region.width
            val regionHeight = region.height
            val subsetPixels = IntArray(regionWidth * regionHeight)

            for (row in 0 until regionHeight) {
                pixels.copyInto(
                    destination = subsetPixels,
                    destinationOffset = row * regionWidth,
                    startIndex = (row + region.top) * width + region.left,
                    endIndex = (row + region.top) * width + region.left + regionWidth,
                )
            }
            return subsetPixels
        }
    }

    public fun interface Filter {
        public fun isAllowed(
            rgb: Int,
            hsl: FloatArray,
        ): Boolean
    }

    public companion object {
        public const val DEFAULT_RESIZE_BITMAP_AREA: Int = 112 * 112
        public const val DEFAULT_CALCULATE_NUMBER_COLORS: Int = 16
        public const val MIN_CONTRAST_TITLE_TEXT: Float = 3.0f
        public const val MIN_CONTRAST_BODY_TEXT: Float = 4.5f

        public fun from(
            pixels: IntArray,
            width: Int,
            height: Int,
        ): Builder = Builder(pixels, width, height)

        public fun from(swatches: List<Swatch>): Palette = Builder(swatches).generate()

        public val DEFAULT_FILTER: Filter =
            object : Filter {
                private val BLACK_MAX_LIGHTNESS = 0.05f
                private val WHITE_MIN_LIGHTNESS = 0.95f

                override fun isAllowed(
                    rgb: Int,
                    hsl: FloatArray,
                ): Boolean = !isWhite(hsl) && !isBlack(hsl) && !isNearRedILine(hsl)

                private fun isBlack(hslColor: FloatArray): Boolean =
                    hslColor[2] <= BLACK_MAX_LIGHTNESS

                private fun isWhite(hslColor: FloatArray): Boolean =
                    hslColor[2] >= WHITE_MIN_LIGHTNESS

                private fun isNearRedILine(hslColor: FloatArray): Boolean =
                    hslColor[0] in 10f..37f && hslColor[1] <= 0.82f
            }
    }
}
