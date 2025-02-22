package me.mitkovic.kmp.currencyconverter.platform

import android.app.Activity
import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.core.view.WindowCompat
import me.mitkovic.kmp.currencyconverter.ui.theme.spacing
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

@Composable
actual fun UpdateStatusBarAppearance(isDarkTheme: Boolean) {
    val context = LocalContext.current
    val view = LocalView.current
    if (context is Activity) {
        DisposableEffect(isDarkTheme) {
            WindowCompat.setDecorFitsSystemWindows(context.window, false)
            // Update status bar icon appearance based on theme.
            WindowCompat.getInsetsController(context.window, view).isAppearanceLightStatusBars = !isDarkTheme
            onDispose { }
        }
    }
}

@Composable
actual fun platformHorizontalPadding(): Dp = MaterialTheme.spacing.medium

actual fun formatNumber(
    value: Double,
    decimals: Int,
): String = String.format(Locale.US, "%.${decimals}f", value)

actual fun formatDateTime(dateTime: Long): String {
    val dateTimeFormat = "MMM d ''yy HH:mm"
    val sdf = SimpleDateFormat(dateTimeFormat, Locale.getDefault())
    return sdf.format(Date(dateTime))
}
