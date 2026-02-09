package com.kmpalette.ui.components

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle

private const val KMPALETTE_URL = "https://github.com/jordond/kmpalette"
private const val MATERIALKOLOR_URL = "https://github.com/jordond/materialkolor"

/**
 * A blurb describing KMPalette and MaterialKolor with clickable links.
 */
@Composable
fun AboutBlurb(modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current
    val primaryColor = MaterialTheme.colorScheme.primary
    val textColor = MaterialTheme.colorScheme.onSurface
    val textStyle = MaterialTheme.typography.bodyMedium.copy(color = textColor)

    val annotatedString = buildAnnotatedString {
        withStyle(SpanStyle(color = textColor)) {
            append("This demo showcases ")
        }

        pushStringAnnotation(tag = "URL", annotation = KMPALETTE_URL)
        withStyle(
            SpanStyle(
                color = primaryColor,
                textDecoration = TextDecoration.Underline,
            ),
        ) {
            append("KMPalette")
        }
        pop()

        withStyle(SpanStyle(color = textColor)) {
            append(", a Compose Multiplatform library for extracting color palettes from images. ")
            append("The dynamic Material3 theme is powered by ")
        }

        pushStringAnnotation(tag = "URL", annotation = MATERIALKOLOR_URL)
        withStyle(
            SpanStyle(
                color = primaryColor,
                textDecoration = TextDecoration.Underline,
            ),
        ) {
            append("MaterialKolor")
        }
        pop()

        withStyle(SpanStyle(color = textColor)) {
            append(", which generates a complete color scheme from any seed color.")
        }
    }

    ClickableText(
        text = annotatedString,
        style = textStyle,
        modifier = modifier,
        onClick = { offset ->
            annotatedString
                .getStringAnnotations(tag = "URL", start = offset, end = offset)
                .firstOrNull()
                ?.let { annotation ->
                    uriHandler.openUri(annotation.item)
                }
        },
    )
}
