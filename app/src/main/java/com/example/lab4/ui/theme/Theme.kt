package com.example.lab4.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColors = darkColorScheme(
    primary = md_theme_primary,
    onPrimary = md_theme_onPrimary,
    background = md_theme_background,
    surface = md_theme_surface,
    onSurface = md_theme_onSurface
)

@Composable
fun Lab4Theme(content: @Composable () -> Unit) {
    val darkTheme = true
    MaterialTheme(
        colorScheme = DarkColors,
        typography = Typography,
        content = content
    )
}
