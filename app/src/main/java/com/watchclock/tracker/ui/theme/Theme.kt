package com.watchclock.tracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = NavyMedium,
    onPrimary = Cream,
    primaryContainer = NavyLight,
    onPrimaryContainer = Cream,
    secondary = Gold,
    onSecondary = NavyDark,
    secondaryContainer = GoldLight,
    onSecondaryContainer = NavyDark,
    background = SurfaceLight,
    onBackground = NavyDark,
    surface = Cream,
    onSurface = NavyDark,
    surfaceVariant = CreamDark,
    onSurfaceVariant = NavyMedium,
    error = ErrorRed,
    outline = NavyLight
)

private val DarkColorScheme = darkColorScheme(
    primary = GoldLight,
    onPrimary = NavyDark,
    primaryContainer = NavyMedium,
    onPrimaryContainer = GoldLight,
    secondary = Gold,
    onSecondary = NavyDark,
    secondaryContainer = NavyLight,
    onSecondaryContainer = GoldLight,
    background = SurfaceDark,
    onBackground = OnSurfaceDark,
    surface = NavyDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = NavyMedium,
    onSurfaceVariant = CreamDark,
    error = Color(0xFFCF6679),
    outline = NavyLight
)

@Composable
fun WatchClockTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = WatchClockTypography,
        content = content
    )
}
