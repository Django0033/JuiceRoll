package com.juiceroll.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Use system fonts:
// - Serif → FontFamily.Serif (Noto Serif on modern Android)
// - Sans → FontFamily.SansSerif (Roboto, ships with all Android devices)
// - Mono → FontFamily.Monospace (system monospace)
private val sansFontFamily = FontFamily.SansSerif
private val serifFontFamily = FontFamily.Serif
private val monoFontFamily = FontFamily.Monospace

val JuiceRollTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = serifFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        color = Parchment
    ),
    displayMedium = TextStyle(
        fontFamily = serifFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        color = Parchment
    ),
    displaySmall = TextStyle(
        fontFamily = serifFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        color = Parchment
    ),
    headlineLarge = TextStyle(
        fontFamily = serifFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = Parchment
    ),
    headlineMedium = TextStyle(
        fontFamily = serifFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        color = Parchment
    ),
    headlineSmall = TextStyle(
        fontFamily = serifFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        color = Parchment
    ),
    titleLarge = TextStyle(
        fontFamily = serifFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = Parchment
    ),
    titleMedium = TextStyle(
        fontFamily = sansFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        color = Parchment
    ),
    titleSmall = TextStyle(
        fontFamily = sansFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        color = Parchment
    ),
    bodyLarge = TextStyle(
        fontFamily = sansFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        color = Parchment.copy(alpha = 0.95f)
    ),
    bodyMedium = TextStyle(
        fontFamily = sansFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        color = Parchment.copy(alpha = 0.90f)
    ),
    bodySmall = TextStyle(
        fontFamily = sansFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        color = Parchment.copy(alpha = 0.70f)
    ),
    labelLarge = TextStyle(
        fontFamily = sansFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        color = Parchment
    ),
    labelMedium = TextStyle(
        fontFamily = sansFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        color = Parchment
    ),
    labelSmall = TextStyle(
        fontFamily = sansFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        color = Parchment.copy(alpha = 0.80f)
    )
)
