package me.mitkovic.kmp.currencyconverter.platform
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import me.mitkovic.kmp.currencyconverter.ui.theme.spacing

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

@Composable
actual fun UpdateStatusBarAppearance(isDarkTheme: Boolean) {
}

@Composable
actual fun platformHorizontalPadding(): Dp = MaterialTheme.spacing.medium
