package me.mitkovic.kmp.currencyconverter.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import currencyconverter_kmp.composeapp.generated.resources.Res
import currencyconverter_kmp.composeapp.generated.resources.no_currency_detail
import me.mitkovic.kmp.currencyconverter.ui.theme.spacing
import me.mitkovic.kmp.currencyconverter.ui.utils.CurrencyInfo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CurrencyWithFlag(currencyCode: String) {
    val currencyDetail = CurrencyInfo.getCurrencyDetail(currencyCode)
    currencyDetail?.let { detail ->
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(detail.flagResId),
                contentDescription = "${detail.fullName} Flag",
                modifier = Modifier.size(MaterialTheme.spacing.large),
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
            Text(
                text = detail.code,
                color = MaterialTheme.colorScheme.onPrimary, // Ensuring text color is onPrimary
            )
        }
    } ?: Text(stringResource(Res.string.no_currency_detail), color = MaterialTheme.colorScheme.onPrimary) // Fallback text
}

@Composable
fun FullCurrencyName(
    modifier: Modifier = Modifier,
    currencyCode: String,
) {
    val currencyDetail = CurrencyInfo.getCurrencyDetail(currencyCode)
    currencyDetail?.let { detail ->
        Text(
            text = detail.fullName,
            modifier = modifier,
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onBackground),
        )
    }
}
