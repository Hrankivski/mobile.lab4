package com.example.lab4.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColorScheme(
    primary = Color(0xFF5FB0FF),
    onPrimary = Color.Black,
    background = Color(0xFF0D1112),
    surface = Color(0xFF111418),
    onSurface = Color(0xFFE8EEF3)
)

@Composable
fun Lab4Theme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColors,
        typography = Typography(),
        content = content
    )
}
