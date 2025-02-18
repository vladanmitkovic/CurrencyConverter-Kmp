package me.mitkovic.kmp.currencyconverter.platform

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import me.mitkovic.kmp.currencyconverter.ui.theme.spacing
import platform.UIKit.UIDevice

class IOSPlatform : Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

@Composable
actual fun UpdateStatusBarAppearance(isDarkTheme: Boolean) {
}

@Composable
actual fun platformHorizontalPadding(): Dp = MaterialTheme.spacing.xLarge
