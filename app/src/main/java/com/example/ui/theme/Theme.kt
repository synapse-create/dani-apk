package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MysticColorScheme = darkColorScheme(
    primary = RadiantGold,
    secondary = SacredPink,
    tertiary = StarlightBlue,
    background = MidnightSpace,
    surface = CelestialViolet,
    onPrimary = MidnightSpace,
    onSecondary = Color.White,
    onBackground = MoonWhite,
    onSurface = MoonWhite,
    outline = DeepAmethyst
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // We override to enforce beautiful spiritual dark aesthetic by default!
    dynamicColor: Boolean = false, // Disable dynamic colors to preserve our gorgeous mystic gold theme
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = MysticColorScheme,
        typography = Typography,
        content = content
    )
}
