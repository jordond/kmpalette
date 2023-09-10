public fun ImageBitmap.generatePalette(
    block: Palette.Builder.() -> Unit = {},
): Palette {
    return Palette.from(this).apply(block).generate()
}
public fun Palette.dominantSwatch(): Palette.Swatch =
    swatches.maxByOrNull { it.population } ?: error("No swatches found")