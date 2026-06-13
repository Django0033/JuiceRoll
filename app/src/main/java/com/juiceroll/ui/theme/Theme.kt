package com.juiceroll.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val JuiceRollColorScheme = darkColorScheme(
    primary = Gold,
    onPrimary = Background,
    primaryContainer = Gold.copy(alpha = 0.15f),
    secondary = JuiceOrange,
    tertiary = Parchment,
    background = Background,
    surface = Surface,
    surfaceVariant = CardSurface,
    onBackground = Parchment,
    onSurface = Parchment,
    onSurfaceVariant = ParchmentDark,
    error = Danger,
    onError = Parchment,
    outline = ParchmentDark.copy(alpha = 0.3f)
)

@Composable
fun JuiceTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = JuiceRollColorScheme,
        typography = JuiceRollTypography,
        content = content
    )
}
