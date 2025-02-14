package me.mitkovic.kmp.currencyconverter.platform
import androidx.compose.runtime.Composable

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

@Composable
actual fun UpdateStatusBarAppearance(isDarkTheme: Boolean) {
}
