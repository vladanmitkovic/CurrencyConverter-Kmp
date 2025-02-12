package me.mitkovic.kmp.currencyconverter.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColors =
    darkColorScheme(
        primary = Color(0xFF90CAF9),
        onPrimary = Color.Black,
        secondary = Color(0xFF81D4FA),
        onSecondary = Color.Black,
        tertiary = Color(0xFF80DEEA),
        onTertiary = Color.Black,
        background = Color(0xFF121212),
        onBackground = Color.White,
        surface = Color(0xFF1E1E1E),
        onSurface = Color.White,
    )

private val LightColors =
    lightColorScheme(
        primary = Color(0xFF1976D2),
        onPrimary = Color.White,
        secondary = Color(0xFF0288D1),
        onSecondary = Color.White,
        tertiary = Color(0xFF0097A7),
        onTertiary = Color.White,
        background = Color.White,
        onBackground = Color.Black,
        surface = Color(0xFFF5F5F5),
        onSurface = Color.Black,
    )

@Composable
fun AppTheme(
    isDarkTheme: Boolean,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (isDarkTheme) DarkColors else LightColors,
        content = content,
    )
}
