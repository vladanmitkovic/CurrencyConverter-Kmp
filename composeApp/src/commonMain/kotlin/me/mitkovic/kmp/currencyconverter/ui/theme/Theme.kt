package me.mitkovic.kmp.currencyconverter.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import me.mitkovic.kmp.currencyconverter.platform.UpdateStatusBarAppearance

val DarkColorScheme =
    darkColorScheme(
        primary = DarkPrimary,
        secondary = DarkSecondary,
        tertiary = DarkTertiary,
        background = DarkBackground,
        onBackground = DarkOnBackground,
    )

val LightColorScheme =
    lightColorScheme(
        primary = LightPrimary,
        secondary = LightSecondary,
        tertiary = LightTertiary,
        background = LightBackground,
        onBackground = LightOnBackground,
    )

@Composable
fun AppTheme(
    isDarkTheme: Boolean,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (isDarkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = {
            // On Android, update system UI appearance
            UpdateStatusBarAppearance(isDarkTheme)
            content()
        },
    )
}
