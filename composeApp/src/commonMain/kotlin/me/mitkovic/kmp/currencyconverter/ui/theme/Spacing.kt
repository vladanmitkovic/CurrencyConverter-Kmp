package me.mitkovic.kmp.currencyconverter.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Spacing(
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val extraMedium: Dp = 10.dp,
    val medium: Dp = 16.dp,
    val large: Dp = 24.dp,
    val xLarge: Dp = 32.dp,
    val iconSize: Dp = 32.dp,
    val largeItem: Dp = 36.dp,
    val bottomBarHeight: Dp = 48.dp,
    val largeButton: Dp = 56.dp,
    val tickerSpeed: Dp = 60.dp,
    val numericButton: Dp = 66.dp,
    val currencySelection: Dp = 86.dp,
    val xxLarge: Dp = 96.dp,
    val xxxLarge: Dp = 128.dp,
)

val LocalSpacing = compositionLocalOf { Spacing() }

val MaterialTheme.spacing: Spacing
    @Composable
    @ReadOnlyComposable
    get() = LocalSpacing.current
