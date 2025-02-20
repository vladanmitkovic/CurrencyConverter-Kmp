package me.mitkovic.kmp.currencyconverter.platform

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import me.mitkovic.kmp.currencyconverter.ui.theme.spacing
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterDecimalStyle
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.Foundation.localeWithLocaleIdentifier
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

actual fun formatNumber(
    value: Double,
    decimals: Int,
): String {
    val formatter =
        NSNumberFormatter().apply {
            numberStyle = NSNumberFormatterDecimalStyle
            minimumFractionDigits = decimals.toULong()
            maximumFractionDigits = decimals.toULong()
            locale = NSLocale.localeWithLocaleIdentifier("en_US")
        }
    return formatter.stringFromNumber(NSNumber(value)) ?: value.toString()
}

actual fun formatDateTime(dateTime: Long): String {
    // Convert milliseconds to seconds for NSDate (which uses seconds)
    val dateInSeconds = dateTime / 1000.0
    val date = NSDate.dateWithTimeIntervalSince1970(dateInSeconds)
    val formatter =
        NSDateFormatter().apply {
            dateFormat = "MMM d ''yy HH:mm"
            locale = NSLocale.localeWithLocaleIdentifier("en_US")
        }
    return formatter.stringFromDate(date)
}
