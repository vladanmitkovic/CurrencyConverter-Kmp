package me.mitkovic.kmp.currencyconverter.platform
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import me.mitkovic.kmp.currencyconverter.ui.theme.spacing
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

@Composable
actual fun UpdateStatusBarAppearance(isDarkTheme: Boolean) {
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
