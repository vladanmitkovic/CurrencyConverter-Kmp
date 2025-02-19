package me.mitkovic.kmp.currencyconverter.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

@Composable
expect fun UpdateStatusBarAppearance(isDarkTheme: Boolean)

@Composable
expect fun platformHorizontalPadding(): Dp

expect fun formatNumber(
    value: Double,
    decimals: Int = 4,
): String
