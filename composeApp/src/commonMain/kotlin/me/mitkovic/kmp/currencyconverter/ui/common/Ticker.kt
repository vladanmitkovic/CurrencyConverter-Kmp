package me.mitkovic.kmp.currencyconverter.ui.common

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import me.mitkovic.kmp.currencyconverter.common.Constants
import me.mitkovic.kmp.currencyconverter.platform.formatNumber
import me.mitkovic.kmp.currencyconverter.ui.theme.spacing

@Composable
fun Ticker(
    rates: Map<String, Double>,
    selectedCurrencyLeft: String,
) {
    // Find the conversion rate for the selected currency relative to the base (EUR)
    val selectedCurrencyRateToBase = rates[selectedCurrencyLeft] ?: 1.0

    val recalculatedRates =
        rates.mapValues { (currency, rate) ->
            if (currency == Constants.BASE_CURRENCY) 1 / selectedCurrencyRateToBase else rate / selectedCurrencyRateToBase
        }

    val annotatedTickerText =
        buildAnnotatedString {
            recalculatedRates
                .filterKeys { it != selectedCurrencyLeft }
                .entries
                .forEachIndexed { index, entry ->
                    if (index > 0) append(" - ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("$selectedCurrencyLeft/${entry.key}")
                    }
                    append(" ${formatNumber(entry.value, 4)}")
                }
        }

    // Ticker at the bottom with specified background and text color
    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier =
            Modifier
                .fillMaxWidth()
                .height(MaterialTheme.spacing.largeItem),
        // Adjust height as desired
    ) {
        Box {
            Text(
                text = annotatedTickerText,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier =
                    Modifier
                        .basicMarquee(
                            iterations = Int.MAX_VALUE,
                            velocity = MaterialTheme.spacing.tickerSpeed,
                        ).align(Alignment.Center),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}
