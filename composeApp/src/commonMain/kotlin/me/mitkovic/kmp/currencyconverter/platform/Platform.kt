package me.mitkovic.kmp.currencyconverter.platform

import androidx.compose.runtime.Composable

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

@Composable
expect fun UpdateStatusBarAppearance(isDarkTheme: Boolean)
