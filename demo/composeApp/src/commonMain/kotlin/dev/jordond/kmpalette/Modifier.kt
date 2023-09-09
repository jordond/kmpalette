package dev.jordond.kmpalette

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

@Composable
fun Modifier.conditional(
    condition: Boolean,
    block: @Composable () -> Modifier,
): Modifier = if (condition) composed { then(block()) } else this