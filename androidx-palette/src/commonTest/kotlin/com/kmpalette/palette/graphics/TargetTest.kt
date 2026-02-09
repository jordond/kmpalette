package com.kmpalette.palette.graphics

import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Tests for Target class - the profiles used to select swatches from a palette.
 */
class TargetTest {
    // ==================== Predefined Targets Tests ====================

    @Test
    fun `LIGHT_VIBRANT target has correct lightness range`() {
        val target = Target.LIGHT_VIBRANT

        assertTrue(target.minimumLightness >= 0.5f, "Light target should have high minimum lightness")
        assertTrue(target.targetLightness >= 0.7f, "Light target should have high target lightness")
    }

    @Test
    fun `VIBRANT target has medium lightness range`() {
        val target = Target.VIBRANT

        assertTrue(target.targetLightness in 0.4f..0.6f, "Vibrant target should have medium lightness")
    }

    @Test
    fun `DARK_VIBRANT target has low lightness range`() {
        val target = Target.DARK_VIBRANT

        assertTrue(target.maximumLightness <= 0.5f, "Dark target should have low maximum lightness")
        assertTrue(target.targetLightness <= 0.3f, "Dark target should have low target lightness")
    }

    @Test
    fun `LIGHT_MUTED target has correct saturation range`() {
        val target = Target.LIGHT_MUTED

        assertTrue(target.minimumLightness >= 0.5f, "Light muted should have high minimum lightness")
        assertTrue(target.targetSaturation < 0.5f, "Muted target should have low target saturation")
    }

    @Test
    fun `MUTED target has low saturation`() {
        val target = Target.MUTED

        assertTrue(target.targetSaturation < 0.5f, "Muted target should have low saturation")
    }

    @Test
    fun `DARK_MUTED target has low lightness and saturation`() {
        val target = Target.DARK_MUTED

        assertTrue(target.maximumLightness <= 0.5f, "Dark muted should have low maximum lightness")
        assertTrue(target.targetSaturation < 0.5f, "Muted target should have low saturation")
    }

    // ==================== Vibrant vs Muted Comparison ====================

    @Test
    fun `vibrant targets have higher saturation than muted targets`() {
        assertTrue(
            Target.VIBRANT.targetSaturation > Target.MUTED.targetSaturation,
            "VIBRANT should have higher saturation than MUTED",
        )
        assertTrue(
            Target.LIGHT_VIBRANT.targetSaturation > Target.LIGHT_MUTED.targetSaturation,
            "LIGHT_VIBRANT should have higher saturation than LIGHT_MUTED",
        )
        assertTrue(
            Target.DARK_VIBRANT.targetSaturation > Target.DARK_MUTED.targetSaturation,
            "DARK_VIBRANT should have higher saturation than DARK_MUTED",
        )
    }

    // ==================== Target Properties Tests ====================

    @Test
    fun `all predefined targets exist`() {
        assertNotNull(Target.LIGHT_VIBRANT)
        assertNotNull(Target.VIBRANT)
        assertNotNull(Target.DARK_VIBRANT)
        assertNotNull(Target.LIGHT_MUTED)
        assertNotNull(Target.MUTED)
        assertNotNull(Target.DARK_MUTED)
    }

    @Test
    fun `targets have valid saturation ranges`() {
        val targets =
            listOf(
                Target.LIGHT_VIBRANT,
                Target.VIBRANT,
                Target.DARK_VIBRANT,
                Target.LIGHT_MUTED,
                Target.MUTED,
                Target.DARK_MUTED,
            )

        for (target in targets) {
            assertTrue(target.minimumSaturation >= 0f, "Min saturation should be >= 0")
            assertTrue(target.maximumSaturation <= 1f, "Max saturation should be <= 1")
            assertTrue(
                target.minimumSaturation <= target.maximumSaturation,
                "Min saturation should be <= max saturation",
            )
            assertTrue(
                target.targetSaturation >= target.minimumSaturation &&
                    target.targetSaturation <= target.maximumSaturation,
                "Target saturation should be within range",
            )
        }
    }

    @Test
    fun `targets have valid lightness ranges`() {
        val targets =
            listOf(
                Target.LIGHT_VIBRANT,
                Target.VIBRANT,
                Target.DARK_VIBRANT,
                Target.LIGHT_MUTED,
                Target.MUTED,
                Target.DARK_MUTED,
            )

        for (target in targets) {
            assertTrue(target.minimumLightness >= 0f, "Min lightness should be >= 0")
            assertTrue(target.maximumLightness <= 1f, "Max lightness should be <= 1")
            assertTrue(
                target.minimumLightness <= target.maximumLightness,
                "Min lightness should be <= max lightness",
            )
            assertTrue(
                target.targetLightness >= target.minimumLightness &&
                    target.targetLightness <= target.maximumLightness,
                "Target lightness should be within range",
            )
        }
    }

    @Test
    fun `targets have non-negative weights`() {
        val targets =
            listOf(
                Target.LIGHT_VIBRANT,
                Target.VIBRANT,
                Target.DARK_VIBRANT,
                Target.LIGHT_MUTED,
                Target.MUTED,
                Target.DARK_MUTED,
            )

        for (target in targets) {
            assertTrue(target.saturationWeight >= 0f, "Saturation weight should be >= 0")
            assertTrue(target.lightnessWeight >= 0f, "Lightness weight should be >= 0")
            assertTrue(target.populationWeight >= 0f, "Population weight should be >= 0")
        }
    }

    // ==================== Custom Target Tests ====================

    @Test
    fun `custom target can be created with Builder`() {
        val customTarget =
            Target
                .Builder()
                .setMinimumSaturation(0.2f)
                .setTargetSaturation(0.6f)
                .setMaximumSaturation(0.9f)
                .setMinimumLightness(0.3f)
                .setTargetLightness(0.5f)
                .setMaximumLightness(0.7f)
                .build()

        assertFloatEquals(0.2f, customTarget.minimumSaturation)
        assertFloatEquals(0.6f, customTarget.targetSaturation)
        assertFloatEquals(0.9f, customTarget.maximumSaturation)
        assertFloatEquals(0.3f, customTarget.minimumLightness)
        assertFloatEquals(0.5f, customTarget.targetLightness)
        assertFloatEquals(0.7f, customTarget.maximumLightness)
    }

    @Test
    fun `custom target weights can be set`() {
        val customTarget =
            Target
                .Builder()
                .setSaturationWeight(0.5f)
                .setLightnessWeight(0.3f)
                .setPopulationWeight(0.2f)
                .build()

        assertFloatEquals(0.5f, customTarget.saturationWeight)
        assertFloatEquals(0.3f, customTarget.lightnessWeight)
        assertFloatEquals(0.2f, customTarget.populationWeight)
    }

    @Test
    fun `exclusive target prevents swatch reuse`() {
        val exclusiveTarget =
            Target
                .Builder()
                .setExclusive(true)
                .build()

        assertTrue(exclusiveTarget.isExclusive, "Target should be exclusive")
    }

    @Test
    fun `non-exclusive target allows swatch reuse`() {
        val nonExclusiveTarget =
            Target
                .Builder()
                .setExclusive(false)
                .build()

        assertTrue(!nonExclusiveTarget.isExclusive, "Target should not be exclusive")
    }

    // FloatArray maps to Float32Array on JS, so round-tripping through it
    // can introduce small precision differences vs double-precision literals.
    private fun assertFloatEquals(
        expected: Float,
        actual: Float,
    ) {
        assertTrue(
            abs(expected - actual) < 1e-5f,
            "Expected <$expected> but was <$actual>",
        )
    }
}
