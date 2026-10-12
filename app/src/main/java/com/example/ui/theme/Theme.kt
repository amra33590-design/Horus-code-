package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = HorusCyan,
    onPrimary = HorusDeepNavy,
    primaryContainer = HorusElevatedNavy,
    onPrimaryContainer = HorusElectricCyan,
    secondary = HorusGold,
    onSecondary = HorusDeepNavy,
    secondaryContainer = HorusCardNavy,
    onSecondaryContainer = HorusLightGold,
    tertiary = HorusElectricCyan,
    onTertiary = HorusDeepNavy,
    background = HorusDeepNavy,
    onBackground = HorusIvory,
    surface = HorusCardNavy,
    onSurface = HorusIvory,
    surfaceVariant = HorusElevatedNavy,
    onSurfaceVariant = HorusMutedIvory,
    outline = HorusSubtleBorder,
    error = HorusErrorCrimson,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF00838F),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0F7FA),
    onPrimaryContainer = Color(0xFF004D40),
    secondary = Color(0xFFD4A017),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFF8E1),
    onSecondaryContainer = Color(0xFF5D4037),
    tertiary = Color(0xFF0097A7),
    onTertiary = Color.White,
    background = HorusLightBg,
    onBackground = HorusLightText,
    surface = HorusLightSurface,
    onSurface = HorusLightText,
    surfaceVariant = Color(0xFFECEFF1),
    onSurfaceVariant = Color(0xFF455A64),
    outline = Color(0xFFCFD8DC),
    error = HorusErrorCrimson,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
