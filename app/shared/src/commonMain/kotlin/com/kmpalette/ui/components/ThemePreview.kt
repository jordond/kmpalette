package com.kmpalette.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kmpalette.ui.util.toHexString
import com.materialkolor.rememberDynamicColorScheme

data class ThemeColor(
    val title: String,
    val swatchNumber: String,
    val color: @Composable () -> Color,
)

data class ThemePair(
    val color: ThemeColor,
    val onColor: ThemeColor,
)

data class ThemeGroup(
    val main: ThemePair,
    val container: ThemePair,
)

private object ThemeColors {
    val Primary = ThemeColor("Primary", "P-40") { MaterialTheme.colorScheme.primary }
    val OnPrimary = ThemeColor("On Primary", "P-100") { MaterialTheme.colorScheme.onPrimary }
    val PrimaryContainer = ThemeColor("Primary Container", "P-90") { MaterialTheme.colorScheme.primaryContainer }
    val OnPrimaryContainer = ThemeColor("On Primary Container", "P-10") { MaterialTheme.colorScheme.onPrimaryContainer }

    val Secondary = ThemeColor("Secondary", "S-40") { MaterialTheme.colorScheme.secondary }
    val OnSecondary = ThemeColor("On Secondary", "S-100") { MaterialTheme.colorScheme.onSecondary }
    val SecondaryContainer = ThemeColor("Secondary Container", "S-90") { MaterialTheme.colorScheme.secondaryContainer }
    val OnSecondaryContainer =
        ThemeColor("On Secondary Container", "S-10") { MaterialTheme.colorScheme.onSecondaryContainer }

    val Tertiary = ThemeColor("Tertiary", "T-40") { MaterialTheme.colorScheme.tertiary }
    val OnTertiary = ThemeColor("On Tertiary", "T-100") { MaterialTheme.colorScheme.onTertiary }
    val TertiaryContainer = ThemeColor("Tertiary Container", "T-90") { MaterialTheme.colorScheme.tertiaryContainer }
    val OnTertiaryContainer =
        ThemeColor("On Tertiary Container", "T-10") { MaterialTheme.colorScheme.onTertiaryContainer }

    val Error = ThemeColor("Error", "E-40") { MaterialTheme.colorScheme.error }
    val OnError = ThemeColor("On Error", "E-100") { MaterialTheme.colorScheme.onError }
    val ErrorContainer = ThemeColor("Error Container", "E-90") { MaterialTheme.colorScheme.errorContainer }
    val OnErrorContainer = ThemeColor("On Error Container", "E-10") { MaterialTheme.colorScheme.onErrorContainer }

    val SurfaceDim = ThemeColor("Surface Dim", "N-87") { MaterialTheme.colorScheme.surfaceDim }
    val Surface = ThemeColor("Surface", "N-98") { MaterialTheme.colorScheme.surface }
    val SurfaceBright = ThemeColor("Surface Bright", "N-98") { MaterialTheme.colorScheme.surfaceBright }

    val SurfaceContainerLowest =
        ThemeColor("Surf Cont Lowest", "N-100") { MaterialTheme.colorScheme.surfaceContainerLowest }
    val SurfaceContainerLow = ThemeColor("Surf Cont Low", "N-96") { MaterialTheme.colorScheme.surfaceContainerLow }
    val SurfaceContainer = ThemeColor("Surf Container", "N-94") { MaterialTheme.colorScheme.surfaceContainer }
    val SurfaceContainerHigh = ThemeColor("Surf Cont High", "N-92") { MaterialTheme.colorScheme.surfaceContainerHigh }
    val SurfaceContainerHighest =
        ThemeColor("Surf Cont Highest", "N-90") { MaterialTheme.colorScheme.surfaceContainerHighest }

    val OnSurface = ThemeColor("On Surface", "N-10") { MaterialTheme.colorScheme.onSurface }
    val OnSurfaceVariant = ThemeColor("On Surface Variant", "NV-30") { MaterialTheme.colorScheme.onSurfaceVariant }
    val Outline = ThemeColor("Outline", "NV-50") { MaterialTheme.colorScheme.outline }
    val OutlineVariant = ThemeColor("Outline Variant", "NV-80") { MaterialTheme.colorScheme.outlineVariant }

    val InverseSurface = ThemeColor("Inverse Surface", "N-20") { MaterialTheme.colorScheme.inverseSurface }
    val InverseOnSurface = ThemeColor("Inverse On Surface", "N-95") { MaterialTheme.colorScheme.inverseOnSurface }
    val InversePrimary = ThemeColor("Inverse Primary", "P-80") { MaterialTheme.colorScheme.inversePrimary }
    val Scrim = ThemeColor("Scrim", "N-0") { MaterialTheme.colorScheme.scrim }
}

private object ThemeGroups {
    val Primary = ThemeGroup(
        main = ThemePair(ThemeColors.Primary, ThemeColors.OnPrimary),
        container = ThemePair(ThemeColors.PrimaryContainer, ThemeColors.OnPrimaryContainer),
    )
    val Secondary = ThemeGroup(
        main = ThemePair(ThemeColors.Secondary, ThemeColors.OnSecondary),
        container = ThemePair(ThemeColors.SecondaryContainer, ThemeColors.OnSecondaryContainer),
    )
    val Tertiary = ThemeGroup(
        main = ThemePair(ThemeColors.Tertiary, ThemeColors.OnTertiary),
        container = ThemePair(ThemeColors.TertiaryContainer, ThemeColors.OnTertiaryContainer),
    )
    val Error = ThemeGroup(
        main = ThemePair(ThemeColors.Error, ThemeColors.OnError),
        container = ThemePair(ThemeColors.ErrorContainer, ThemeColors.OnErrorContainer),
    )
}

private object ThemeSectionDefaults {
    val SectionDivider = 16.dp
    val InnerDivider = 6.dp
    val BoxPadding = 12.dp

    val MainColors = listOf(ThemeGroups.Primary, ThemeGroups.Secondary, ThemeGroups.Tertiary)

    val SurfaceColors = listOf(
        ThemeColors.SurfaceDim,
        ThemeColors.Surface,
        ThemeColors.SurfaceBright,
    )

    val SurfaceContainerColors = listOf(
        ThemeColors.SurfaceContainerLowest,
        ThemeColors.SurfaceContainerLow,
        ThemeColors.SurfaceContainer,
        ThemeColors.SurfaceContainerHigh,
        ThemeColors.SurfaceContainerHighest,
    )

    val InverseSurfacePair = ThemePair(ThemeColors.InverseSurface, ThemeColors.InverseOnSurface)

    @Composable
    fun Color.inverse(): Color = if (luminance() > 0.5f) Color.Black else Color.White
}

@Composable
private fun CopyIcon(
    visible: Boolean,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier,
    ) {
        Icon(
            imageVector = Icons.Default.ContentCopy,
            contentDescription = "Copy color code",
            modifier = Modifier.size(16.dp),
        )
    }
}

@Composable
fun ThemePreviewSection(
    seedColor: Color,
    modifier: Modifier = Modifier,
    onColorCopied: ((String) -> Unit)? = null,
) {
    val lightScheme = rememberDynamicColorScheme(seedColor, isDark = false, isAmoled = false)
    val darkScheme = rememberDynamicColorScheme(seedColor, isDark = true, isAmoled = false)
    val clipboardManager = LocalClipboardManager.current

    val handleCopyColor: (String, Color) -> Unit = { name, color ->
        val hex = color.toHexString()
        clipboardManager.setText(AnnotatedString(hex))
        onColorCopied?.invoke("Copied $name: $hex")
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
            MaterialTheme(colorScheme = lightScheme) {
                ThemeSectionContent(
                    title = "Light Theme",
                    onCopyColor = handleCopyColor,
                )
            }
        }

        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
            MaterialTheme(colorScheme = darkScheme) {
                ThemeSectionContent(
                    title = "Dark Theme",
                    onCopyColor = handleCopyColor,
                )
            }
        }
    }
}

@Composable
private fun ThemeSectionContent(
    title: String,
    onCopyColor: (String, Color) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(ThemeSectionDefaults.SectionDivider)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = ThemeSectionDefaults.SectionDivider),
        )

        CompositionLocalProvider(
            LocalTextStyle provides MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(ThemeSectionDefaults.SectionDivider),
                modifier = Modifier.fillMaxWidth(),
            ) {
                // Left section: Primary, Secondary, Tertiary + Surface rows
                Column(
                    verticalArrangement = Arrangement.spacedBy(ThemeSectionDefaults.SectionDivider),
                    modifier = Modifier.weight(1f),
                ) {
                    // Main color groups row
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(ThemeSectionDefaults.InnerDivider),
                    ) {
                        ThemeSectionDefaults.MainColors.forEach { group ->
                            ColorGroupContainer(
                                group = group,
                                onClick = onCopyColor,
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }

                    // Surface colors section
                    Column(
                        verticalArrangement = Arrangement.spacedBy(ThemeSectionDefaults.InnerDivider),
                    ) {
                        // Surface row
                        Row {
                            ThemeSectionDefaults.SurfaceColors.forEach { themeColor ->
                                ColorBox(
                                    themeColor = themeColor,
                                    onClick = onCopyColor,
                                    lines = 4,
                                    modifier = Modifier.weight(1f),
                                )
                            }
                        }

                        // Surface Container row
                        Row {
                            ThemeSectionDefaults.SurfaceContainerColors.forEach { themeColor ->
                                ColorBox(
                                    themeColor = themeColor,
                                    onClick = onCopyColor,
                                    lines = 4,
                                    modifier = Modifier.weight(1f),
                                )
                            }
                        }

                        // Misc colors row
                        Row {
                            MiscColorBox(
                                themeColor = ThemeColors.OnSurface,
                                textColor = MaterialTheme.colorScheme.surface,
                                onClick = onCopyColor,
                                modifier = Modifier.weight(1f),
                            )
                            MiscColorBox(
                                themeColor = ThemeColors.OnSurfaceVariant,
                                textColor = MaterialTheme.colorScheme.surfaceVariant,
                                onClick = onCopyColor,
                                modifier = Modifier.weight(1f),
                            )
                            MiscColorBox(
                                themeColor = ThemeColors.Outline,
                                textColor = with(ThemeSectionDefaults) { MaterialTheme.colorScheme.outline.inverse() },
                                onClick = onCopyColor,
                                modifier = Modifier.weight(1f),
                            )
                            MiscColorBox(
                                themeColor = ThemeColors.OutlineVariant,
                                textColor = with(
                                    ThemeSectionDefaults,
                                ) { MaterialTheme.colorScheme.outlineVariant.inverse() },
                                onClick = onCopyColor,
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }

                // Right section: Error + Inverse colors
                Column(
                    verticalArrangement = Arrangement.spacedBy(ThemeSectionDefaults.SectionDivider),
                    modifier = Modifier.weight(0.3f),
                ) {
                    ColorGroupContainer(
                        group = ThemeGroups.Error,
                        onClick = onCopyColor,
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(ThemeSectionDefaults.InnerDivider),
                    ) {
                        ColorPairContainer(
                            pair = ThemeSectionDefaults.InverseSurfacePair,
                            onClick = onCopyColor,
                        )

                        SingleLineColorBox(
                            themeColor = ThemeColors.InversePrimary,
                            onClick = onCopyColor,
                            modifier = Modifier.fillMaxWidth(),
                        )

                        SingleLineColorBox(
                            themeColor = ThemeColors.Scrim,
                            onClick = onCopyColor,
                            textColor = with(ThemeSectionDefaults) { ThemeColors.Scrim.color().inverse() },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ColorGroupContainer(
    group: ThemeGroup,
    onClick: (String, Color) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(ThemeSectionDefaults.InnerDivider),
        modifier = modifier,
    ) {
        ColorPairContainer(pair = group.main, onClick = onClick)
        ColorPairContainer(pair = group.container, onClick = onClick)
    }
}

@Composable
private fun ColorPairContainer(
    pair: ThemePair,
    onClick: (String, Color) -> Unit,
    modifier: Modifier = Modifier,
    lines: Int = 3,
) {
    Column(modifier = modifier) {
        CompositionLocalProvider(LocalContentColor provides pair.onColor.color()) {
            ColorBox(
                themeColor = pair.color,
                onClick = onClick,
                lines = lines,
                modifier = Modifier.fillMaxSize(),
            )
        }

        CompositionLocalProvider(LocalContentColor provides pair.color.color()) {
            SingleLineColorBox(
                themeColor = pair.onColor,
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun ColorBox(
    themeColor: ThemeColor,
    onClick: (String, Color) -> Unit,
    lines: Int = 3,
    modifier: Modifier = Modifier,
    textColor: Color? = null,
) {
    val color = themeColor.color()
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    ContentColorWrapper(themeColor, textColor) {
        Box(
            modifier
                .background(themeColor.color())
                .hoverable(interactionSource)
                .clickable { onClick(themeColor.title, color) }
                .padding(ThemeSectionDefaults.BoxPadding),
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize(),
            ) {
                Text(
                    text = themeColor.title,
                    minLines = lines,
                    maxLines = lines,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = themeColor.swatchNumber,
                    modifier = Modifier.align(Alignment.End),
                )
            }

            CopyIcon(
                visible = isHovered,
                modifier = Modifier.align(Alignment.TopEnd),
            )
        }
    }
}

@Composable
private fun SingleLineColorBox(
    themeColor: ThemeColor,
    onClick: (String, Color) -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color? = null,
) {
    val color = themeColor.color()
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    ContentColorWrapper(themeColor, textColor) {
        Box(
            modifier = modifier
                .background(themeColor.color())
                .hoverable(interactionSource)
                .clickable { onClick(themeColor.title, color) }
                .padding(ThemeSectionDefaults.BoxPadding),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = themeColor.title,
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = themeColor.swatchNumber,
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                )
            }

            CopyIcon(
                visible = isHovered,
                modifier = Modifier.align(Alignment.CenterEnd).padding(end = 24.dp),
            )
        }
    }
}

@Composable
private fun MiscColorBox(
    themeColor: ThemeColor,
    textColor: Color,
    onClick: (String, Color) -> Unit,
    modifier: Modifier = Modifier,
) {
    val color = themeColor.color()
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    CompositionLocalProvider(LocalContentColor provides textColor) {
        Box(
            modifier = modifier
                .background(themeColor.color())
                .hoverable(interactionSource)
                .clickable { onClick(themeColor.title, color) }
                .padding(ThemeSectionDefaults.BoxPadding),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = themeColor.title,
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = themeColor.swatchNumber,
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                )
            }

            CopyIcon(
                visible = isHovered,
                modifier = Modifier.align(Alignment.CenterEnd).padding(end = 24.dp),
            )
        }
    }
}

@Composable
private fun ContentColorWrapper(
    themeColor: ThemeColor,
    textColor: Color? = null,
    content: @Composable () -> Unit,
) {
    val contentColor by animateColorAsState(
        targetValue = textColor ?: contentColorFor(themeColor.color()),
        label = "contentColor",
    )

    CompositionLocalProvider(LocalContentColor provides contentColor) {
        content()
    }
}
