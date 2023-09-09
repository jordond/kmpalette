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
package dev.jordond.kmpalette.palette.graphics

import androidx.annotation.ColorInt
import androidx.collection.SimpleArrayMap
import androidx.collection.SparseArrayCompat
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ImageBitmap
import dev.jordond.kmpalette.palette.graphics.Palette.Builder
import dev.jordond.kmpalette.palette.internal.scale
import dev.jordond.kmpalette.palette.utils.ColorUtils
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

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
 * Palette p = Palette.from(bitmap).generate();
 * ```
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
class Palette internal constructor(
    /**
     * Returns the dominant swatch from the palette.
     *
     *
     * The dominant swatch is defined as the swatch with the greatest population (frequency)
     * within the palette.
     */
    private val swatches: List<Swatch>,

    /**
     * Returns all of the swatches which make up the palette.
     */
    private val targets: List<Target>,
) {

    private val selectedSwatches: SimpleArrayMap<Target, Swatch?> = SimpleArrayMap()
    private val usedColors: SparseArrayCompat<Boolean> = SparseArrayCompat()

    val dominantSwatch: Swatch? = findDominantSwatch()

    /**
     * Returns the most vibrant swatch in the palette. Might be null.
     *
     * @see Target.VIBRANT
     */
    val vibrantSwatch: Swatch?
        get() = getSwatchForTarget(Target.VIBRANT)

    /**
     * Returns a light and vibrant swatch from the palette. Might be null.
     *
     * @see Target.LIGHT_VIBRANT
     */
    val lightVibrantSwatch: Swatch?
        get() = getSwatchForTarget(Target.LIGHT_VIBRANT)

    /**
     * Returns a dark and vibrant swatch from the palette. Might be null.
     *
     * @see Target.DARK_VIBRANT
     */
    val darkVibrantSwatch: Swatch?
        get() = getSwatchForTarget(Target.DARK_VIBRANT)

    /**
     * Returns a muted swatch from the palette. Might be null.
     *
     * @see Target.MUTED
     */
    val mutedSwatch: Swatch?
        get() = getSwatchForTarget(Target.MUTED)

    /**
     * Returns a muted and light swatch from the palette. Might be null.
     *
     * @see Target.LIGHT_MUTED
     */
    val lightMutedSwatch: Swatch?
        get() = getSwatchForTarget(Target.LIGHT_MUTED)

    /**
     * Returns a muted and dark swatch from the palette. Might be null.
     *
     * @see Target.DARK_MUTED
     */
    val darkMutedSwatch: Swatch?
        get() = getSwatchForTarget(Target.DARK_MUTED)

    /**
     * Returns the most vibrant color in the palette as an RGB packed int.
     *
     * @param defaultColor value to return if the swatch isn't available
     * @see .getVibrantSwatch
     */
    @ColorInt
    fun getVibrantColor(@ColorInt defaultColor: Int): Int {
        return getColorForTarget(Target.VIBRANT, defaultColor)
    }

    /**
     * Returns a light and vibrant color from the palette as an RGB packed int.
     *
     * @param defaultColor value to return if the swatch isn't available
     * @see .getLightVibrantSwatch
     */
    @ColorInt
    fun getLightVibrantColor(@ColorInt defaultColor: Int): Int {
        return getColorForTarget(Target.LIGHT_VIBRANT, defaultColor)
    }

    /**
     * Returns a dark and vibrant color from the palette as an RGB packed int.
     *
     * @param defaultColor value to return if the swatch isn't available
     * @see .getDarkVibrantSwatch
     */
    @ColorInt
    fun getDarkVibrantColor(@ColorInt defaultColor: Int): Int {
        return getColorForTarget(Target.DARK_VIBRANT, defaultColor)
    }

    /**
     * Returns a muted color from the palette as an RGB packed int.
     *
     * @param defaultColor value to return if the swatch isn't available
     * @see .getMutedSwatch
     */
    @ColorInt
    fun getMutedColor(@ColorInt defaultColor: Int): Int {
        return getColorForTarget(Target.MUTED, defaultColor)
    }

    /**
     * Returns a muted and light color from the palette as an RGB packed int.
     *
     * @param defaultColor value to return if the swatch isn't available
     * @see .getLightMutedSwatch
     */
    @ColorInt
    fun getLightMutedColor(@ColorInt defaultColor: Int): Int {
        return getColorForTarget(Target.LIGHT_MUTED, defaultColor)
    }

    /**
     * Returns a muted and dark color from the palette as an RGB packed int.
     *
     * @param defaultColor value to return if the swatch isn't available
     * @see .getDarkMutedSwatch
     */
    @ColorInt
    fun getDarkMutedColor(@ColorInt defaultColor: Int): Int {
        return getColorForTarget(Target.DARK_MUTED, defaultColor)
    }

    /**
     * Returns the selected swatch for the given target from the palette, or `null` if one
     * could not be found.
     */
    fun getSwatchForTarget(target: Target): Swatch? {
        return selectedSwatches[target]
    }

    /**
     * Returns the selected color for the given target from the palette as an RGB packed int.
     *
     * @param defaultColor value to return if the swatch isn't available
     */
    @ColorInt
    fun getColorForTarget(target: Target, @ColorInt defaultColor: Int): Int {
        val swatch = getSwatchForTarget(target)
        return swatch?.rgb ?: defaultColor
    }

    /**
     * Returns the color of the dominant swatch from the palette, as an RGB packed int.
     *
     * @param defaultColor value to return if the swatch isn't available
     * @see .getDominantSwatch
     */
    @ColorInt
    fun getDominantColor(@ColorInt defaultColor: Int): Int {
        return dominantSwatch?.rgb ?: defaultColor
    }

    fun generate() {
        // TODO(b/141959297): Suppressed during upgrade to AGP 3.6.
        // We need to make sure that the scored targets are generated first. This is so that
        // inherited targets have something to inherit from
        var i = 0
        val count = targets.size
        while (i < count) {
            val target = targets[i]
            target.normalizeWeights()
            selectedSwatches.put(target, generateScoredTarget(target))
            i++
        }

        // We now clear out the used colors
        usedColors.clear()
    }

    private fun generateScoredTarget(target: Target): Swatch? {
        val maxScoreSwatch = getMaxScoredSwatchForTarget(target)
        if (maxScoreSwatch != null && target.isExclusive) {
            // If we have a swatch, and the target is exclusive, add the color to the used list
            usedColors.append(maxScoreSwatch.rgb, true)
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

    private fun shouldBeScoredForTarget(swatch: Swatch, target: Target): Boolean {
        // Check whether the HSL values are within the correct ranges, and this color hasn't
        // been used yet.
        val hsl = swatch.hsl
        return hsl[1] >= target.minimumSaturation
            && hsl[1] <= target.maximumSaturation
            && hsl[2] >= target.minimumLightness
            && hsl[2] <= target.maximumLightness
            && usedColors[swatch.rgb]?.not() ?: false
    }

    private fun generateScore(swatch: Swatch, target: Target): Float {
        val hsl = swatch.hsl
        var saturationScore = 0f
        var luminanceScore = 0f
        var populationScore = 0f
        val maxPopulation = dominantSwatch?.population ?: 1
        if (target.saturationWeight > 0) {
            saturationScore = (target.saturationWeight
                * (1f - abs(hsl[1] - target.targetSaturation)))
        }
        if (target.lightnessWeight > 0) {
            luminanceScore = (target.lightnessWeight
                * (1f - abs(hsl[2] - target.targetLightness)))
        }
        if (target.populationWeight > 0) {
            populationScore = (target.populationWeight
                * (swatch.population / maxPopulation.toFloat()))
        }
        return saturationScore + luminanceScore + populationScore
    }

    private fun findDominantSwatch(): Swatch? {
        var maxPop = Int.MIN_VALUE
        var maxSwatch: Swatch? = null
        var i = 0
        val count = swatches.size
        while (i < count) {
            val swatch = swatches[i]
            if (swatch.population > maxPop) {
                maxSwatch = swatch
                maxPop = swatch.population
            }
            i++
        }
        return maxSwatch
    }

    /**
     * Represents a color swatch generated from an image's palette. The RGB color can be retrieved
     * by calling [.getRgb].
     */
    data class Swatch(
        /**
         * Color Int
         *
         * @return this swatch's RGB color value
         */
        val rgb: Int,

        /**
         * @return the number of pixels represented by this swatch
         */
        val population: Int,
    ) {

        private val red: Int = ColorUtils.red(rgb)
        private val green: Int = ColorUtils.green(rgb)
        private val blue: Int = ColorUtils.blue(rgb)

        private var generatedTextColors = false
        private var mTitleTextColor = 0
        private var mBodyTextColor = 0

        /**
         * Return this swatch's HSL values.
         * hsv[0] is Hue [0 .. 360)
         * hsv[1] is Saturation [0...1]
         * hsv[2] is Lightness [0...1]
         */
        var hsl: FloatArray = FloatArray(3)
            .apply { ColorUtils.RGBToHSL(red, green, blue, this) }
            private set

        /**
         * Returns an appropriate color to use for any 'title' text which is displayed over this
         * [Swatch]'s color. This color is guaranteed to have sufficient contrast.
         */
        val titleTextColor: Int
            get() {
                ensureTextColorsGenerated()
                return mTitleTextColor
            }

        /**
         * Returns an appropriate color to use for any 'body' text which is displayed over this
         * [Swatch]'s color. This color is guaranteed to have sufficient contrast.
         */
        val bodyTextColor: Int
            get() {
                ensureTextColorsGenerated()
                return mBodyTextColor
            }

        private fun ensureTextColorsGenerated() {
            if (!generatedTextColors) {
                // First check white, as most colors will be dark
                val lightBodyAlpha: Int = ColorUtils.calculateMinimumAlpha(
                    ColorUtils.WHITE, rgb, MIN_CONTRAST_BODY_TEXT)
                val lightTitleAlpha: Int = ColorUtils.calculateMinimumAlpha(
                    ColorUtils.WHITE, rgb, MIN_CONTRAST_TITLE_TEXT)
                if (lightBodyAlpha != -1 && lightTitleAlpha != -1) {
                    // If we found valid light values, use them and return
                    mBodyTextColor = ColorUtils.setAlphaComponent(ColorUtils.WHITE, lightBodyAlpha)
                    mTitleTextColor = ColorUtils.setAlphaComponent(ColorUtils.WHITE, lightTitleAlpha)
                    generatedTextColors = true
                    return
                }
                val darkBodyAlpha: Int = ColorUtils.calculateMinimumAlpha(
                    ColorUtils.BLACK, rgb, MIN_CONTRAST_BODY_TEXT)
                val darkTitleAlpha: Int = ColorUtils.calculateMinimumAlpha(
                    ColorUtils.BLACK, rgb, MIN_CONTRAST_TITLE_TEXT)
                if (darkBodyAlpha != -1 && darkTitleAlpha != -1) {
                    // If we found valid dark values, use them and return
                    mBodyTextColor = ColorUtils.setAlphaComponent(ColorUtils.BLACK, darkBodyAlpha)
                    mTitleTextColor = ColorUtils.setAlphaComponent(ColorUtils.BLACK, darkTitleAlpha)
                    generatedTextColors = true
                    return
                }

                // If we reach here then we can not find title and body values which use the same
                // lightness, we need to use mismatched values
                mBodyTextColor =
                    if (lightBodyAlpha != -1) ColorUtils.setAlphaComponent(ColorUtils.WHITE, lightBodyAlpha)
                    else ColorUtils.setAlphaComponent(ColorUtils.BLACK, darkBodyAlpha)

                mTitleTextColor =
                    if (lightTitleAlpha != -1) ColorUtils.setAlphaComponent(ColorUtils.WHITE, lightTitleAlpha)
                    else ColorUtils.setAlphaComponent(ColorUtils.BLACK, darkTitleAlpha)
                generatedTextColors = true
            }
        }
    }

    /**
     * Builder class for generating [Palette] instances.
     */
    class Builder {

        private val mSwatches: List<Swatch>?
        private val mBitmap: ImageBitmap?
        private val mTargets: MutableList<Target> = mutableListOf()
        private var mMaxColors = DEFAULT_CALCULATE_NUMBER_COLORS
        private var mResizeArea = DEFAULT_RESIZE_BITMAP_AREA
        private var mResizeMaxDimension = -1
        private val mFilters: MutableList<Filter> = mutableListOf()
        private var mRegion: Rect? = null

        /**
         * Construct a new [Builder] using a source [ImageBitmap]
         */
        constructor(bitmap: ImageBitmap) {
//            if (bitmap.isNull || bitmap.isClosed) {
//                throw IllegalArgumentException("Bitmap is not valid")
//            }
            mFilters.add(DEFAULT_FILTER)
            mBitmap = bitmap
            mSwatches = null

            // Add the default targets
            mTargets.add(Target.LIGHT_VIBRANT)
            mTargets.add(Target.VIBRANT)
            mTargets.add(Target.DARK_VIBRANT)
            mTargets.add(Target.LIGHT_MUTED)
            mTargets.add(Target.MUTED)
            mTargets.add(Target.DARK_MUTED)
        }

        /**
         * Construct a new [Builder] using a list of [Swatch] instances.
         * Typically only used for testing.
         */
        constructor(swatches: List<Swatch>) {
            if (swatches.isEmpty()) {
                throw IllegalArgumentException("List of Swatches is not valid")
            }
            mFilters.add(DEFAULT_FILTER)
            mSwatches = swatches
            mBitmap = null
        }

        /**
         * Set the maximum number of colors to use in the quantization step when using a
         * [ImageBitmap] as the source.
         *
         *
         * Good values for depend on the source image type. For landscapes, good values are in
         * the range 10-16. For images which are largely made up of people's faces then this
         * value should be increased to ~24.
         */
        fun maximumColorCount(colors: Int): Builder {
            mMaxColors = colors
            return this
        }

        /**
         * Set the resize value when using a [ImageBitmap] as the source.
         * If the bitmap's largest dimension is greater than the value specified, then the bitmap
         * will be resized so that its largest dimension matches `maxDimension`. If the
         * bitmap is smaller or equal, the original is used as-is.
         *
         * @param maxDimension the number of pixels that the max dimension should be scaled down to,
         * or any value <= 0 to disable resizing.
         */
        @Deprecated("Using {@link #resizeBitmapArea(int)} is preferred since it can handle\n"
            + " abnormal aspect ratios more gracefully.")
        fun resizeBitmapSize(maxDimension: Int): Builder {
            mResizeMaxDimension = maxDimension
            mResizeArea = -1
            return this
        }

        /**
         * Set the resize value when using a [ImageBitmap] as the source.
         * If the bitmap's area is greater than the value specified, then the bitmap
         * will be resized so that its area matches `area`. If the
         * bitmap is smaller or equal, the original is used as-is.
         *
         *
         * This value has a large effect on the processing time. The larger the resized image is,
         * the greater time it will take to generate the palette. The smaller the image is, the
         * more detail is lost in the resulting image and thus less precision for color selection.
         *
         * @param area the number of pixels that the intermediary scaled down Bitmap should cover,
         * or any value <= 0 to disable resizing.
         */
        fun resizeBitmapArea(area: Int): Builder {
            mResizeArea = area
            mResizeMaxDimension = -1
            return this
        }

        /**
         * Clear all added filters. This includes any default filters added automatically by
         * [Palette].
         */
        fun clearFilters(): Builder {
            mFilters.clear()
            return this
        }

        /**
         * Add a filter to be able to have fine grained control over which colors are
         * allowed in the resulting palette.
         *
         * @param filter filter to add.
         */
        fun addFilter(filter: Filter): Builder {
            mFilters.add(filter)
            return this
        }

        /**
         * Set a region of the bitmap to be used exclusively when calculating the palette.
         *
         * This only works when the original input is a [ImageBitmap].
         *
         * @param left The left side of the rectangle used for the region.
         * @param top The top of the rectangle used for the region.
         * @param right The right side of the rectangle used for the region.
         * @param bottom The bottom of the rectangle used for the region.
         */
        fun setRegion(left: Int, top: Int, right: Int, bottom: Int): Builder {
            val bitmap = mBitmap
            if (bitmap != null) {
                if (mRegion == null) {
                    mRegion = Rect(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat())
                }

                // Now just get the intersection with the region
                val other = Rect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
                if (!mRegion!!.overlaps(other)) {
                    throw IllegalArgumentException("The given region must intersect with "
                        + "the Bitmap's dimensions.")
                }
            }
            return this
        }

        /**
         * Clear any previously region set via [.setRegion].
         */
        fun clearRegion(): Builder {
            mRegion = null
            return this
        }

        /**
         * Add a target profile to be generated in the palette.
         *
         *
         * You can retrieve the result via [Palette.getSwatchForTarget].
         */
        fun addTarget(target: Target): Builder {
            if (!mTargets.contains(target)) {
                mTargets.add(target)
            }
            return this
        }

        /**
         * Clear all added targets. This includes any default targets added automatically by
         * [Palette].
         */
        fun clearTargets(): Builder {
            mTargets.clear()
            return this
        }

        /**
         * Generate and return the [Palette] synchronously.
         */
        fun generate(): Palette {
            val swatches: List<Swatch>
            if (mBitmap != null) {
                // We have a Bitmap so we need to use quantization to reduce the number of colors

                // First we'll scale down the bitmap if needed
                val bitmap: ImageBitmap = scaleBitmapDown(mBitmap)
                val region: Rect? = mRegion
                if (bitmap != mBitmap && region != null) {
                    // If we have a scaled bitmap and a selected region, we need to scale down the
                    // region to match the new scale
                    val scale: Float = bitmap.width / mBitmap.width.toFloat()
                    mRegion = region.copy(
                        left = floor(region.left * scale),
                        top = floor(region.top * scale),
                        right = min(ceil(region.right * scale), bitmap.width.toFloat()),
                        bottom = min(ceil(region.bottom * scale), bitmap.height.toFloat()),
                    )
                }

                // Now generate a quantizer from the Bitmap
                val quantizer = ColorCutQuantizer(
                    pixels = getPixelsFromBitmap(bitmap),
                    maxColors = mMaxColors,
                    filters = if (mFilters.isEmpty()) null else mFilters.toTypedArray(),
                )

//                // If created a new bitmap, recycle it
//                if (bitmap != mBitmap) {
//                    bitmap.close()
//                }
                swatches = quantizer.quantizedColors
            } else if (mSwatches != null) {
                // Else we're using the provided swatches
                swatches = mSwatches
            } else {
                // The constructors enforce either a bitmap or swatches are present.
                throw AssertionError()
            }

            // Now create a Palette instance
            val p = Palette(swatches, mTargets)
            // And make it generate itself
            p.generate()
            return p
        }

        private fun getPixelsFromBitmap(bitmap: ImageBitmap): IntArray {
            val bitmapWidth: Int = bitmap.width
            val bitmapHeight: Int = bitmap.height
            val pixels = IntArray(bitmapWidth * bitmapHeight)
            bitmap.readPixels(
                buffer = pixels,
                startX = 0,
                startY = 0,
            )

            val region = mRegion
            return if (region == null) {
                // If we don't have a region, return all of the pixels
                pixels
            } else {
                // If we do have a region, lets create a subset array containing only the region's
                // pixels
                val regionWidth = region.width
                val regionHeight = region.height
                // pixels contains all of the pixels, so we need to iterate through each row and
                // copy the regions pixels into a new smaller array
                val subsetPixels = IntArray((regionWidth * regionHeight).toInt())
                for (row in 0 until regionHeight.toInt()) {
                    pixels.copyInto(
                        destination = subsetPixels,
                        destinationOffset = (row * regionWidth).toInt(),
                        startIndex = ((row + region.top) * bitmapWidth + region.left).toInt(),
                        endIndex = ((row + region.top) * bitmapWidth + region.left + regionWidth).toInt()
                    )
                }
                subsetPixels
            }
        }

        /**
         * Scale the bitmap down as needed.
         */
        private fun scaleBitmapDown(bitmap: ImageBitmap): ImageBitmap {
            var scaleRatio = -1.0
            if (mResizeArea > 0) {
                val bitmapArea: Int = bitmap.width * bitmap.height
                if (bitmapArea > mResizeArea) {
                    scaleRatio = sqrt(mResizeArea / bitmapArea.toDouble())
                }
            } else if (mResizeMaxDimension > 0) {
                val maxDimension: Int = max(bitmap.width, bitmap.height)
                if (maxDimension > mResizeMaxDimension) {
                    scaleRatio = mResizeMaxDimension / maxDimension.toDouble()
                }
            }
            return if (scaleRatio <= 0) {
                // Scaling has been disabled or not needed so just return the Bitmap
                bitmap
            } else {
                bitmap.scale(
                    width = ceil(bitmap.width * scaleRatio).toInt(),
                    height = ceil(bitmap.height * scaleRatio).toInt(),
                )
            }
        }
    }

    /**
     * A Filter provides a mechanism for exercising fine-grained control over which colors
     * are valid within a resulting [Palette].
     */
    interface Filter {

        /**
         * Hook to allow clients to be able filter colors from resulting palette.
         *
         * @param rgb the color in RGB888.
         * @param hsl HSL representation of the color.
         *
         * @return true if the color is allowed, false if not.
         *
         * @see Builder.addFilter
         */
        fun isAllowed(rgb: Int, hsl: FloatArray): Boolean
    }

    companion object {

        const val DEFAULT_RESIZE_BITMAP_AREA = 112 * 112
        const val DEFAULT_CALCULATE_NUMBER_COLORS = 16
        const val MIN_CONTRAST_TITLE_TEXT = 3.0f
        const val MIN_CONTRAST_BODY_TEXT = 4.5f

        /**
         * Start generating a [Palette] with the returned [Builder] instance.
         */
        fun from(bitmap: ImageBitmap): Builder {
            return Builder(bitmap)
        }

        /**
         * Generate a [Palette] from the pre-generated list of [Palette.Swatch] swatches.
         * This is useful for testing, or if you want to resurrect a [Palette] instance from a
         * list of swatches. Will return null if the `swatches` is null.
         */
        fun from(swatches: List<Swatch>): Palette {
            return Builder(swatches).generate()
        }

        /**
         * The default filter.
         */
        val DEFAULT_FILTER: Filter = object : Filter {
            private val BLACK_MAX_LIGHTNESS = 0.05f
            private val WHITE_MIN_LIGHTNESS = 0.95f
            override fun isAllowed(rgb: Int, hsl: FloatArray): Boolean {
                return !isWhite(hsl) && !isBlack(hsl) && !isNearRedILine(hsl)
            }

            /**
             * @return true if the color represents a color which is close to black.
             */
            private fun isBlack(hslColor: FloatArray): Boolean {
                return hslColor[2] <= BLACK_MAX_LIGHTNESS
            }

            /**
             * @return true if the color represents a color which is close to white.
             */
            private fun isWhite(hslColor: FloatArray): Boolean {
                return hslColor[2] >= WHITE_MIN_LIGHTNESS
            }

            /**
             * @return true if the color lies close to the red side of the I line.
             */
            private fun isNearRedILine(hslColor: FloatArray): Boolean {
                return hslColor[0] in 10f..37f && hslColor[1] <= 0.82f
            }
        }
    }
}
