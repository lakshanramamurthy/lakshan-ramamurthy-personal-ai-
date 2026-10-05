package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val JarvisDarkColorScheme = darkColorScheme(
    primary = BrandAccent,
    onPrimary = Color.White,
    primaryContainer = BrandCard,
    onPrimaryContainer = BrandAccentLight,
    secondary = ActiveGreen,
    onSecondary = Color.White,
    secondaryContainer = ActiveGreenBg,
    onSecondaryContainer = ActiveGreen,
    tertiary = BrandAccentLight,
    onTertiary = BrandCharcoal,
    background = BrandCharcoal,
    onBackground = TextPrimary,
    surface = BrandSurface,
    onSurface = TextPrimary,
    surfaceVariant = BrandCard,
    onSurfaceVariant = TextMuted,
    outline = BrandBorder,
    outlineVariant = BrandBorderHover
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // Executive Workspace is permanently dark luxury mode
    MaterialTheme(
        colorScheme = JarvisDarkColorScheme,
        typography = Typography,
        content = content
    )
}
