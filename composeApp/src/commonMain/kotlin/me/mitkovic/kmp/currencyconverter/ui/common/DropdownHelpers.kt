package me.mitkovic.kmp.currencyconverter.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.mitkovic.kmp.currencyconverter.ui.theme.spacing
import me.mitkovic.kmp.currencyconverter.ui.utils.CurrencyInfo
import org.jetbrains.compose.resources.painterResource

@Composable
fun CustomDropdownMenuItem(
    currencyCode: String,
    onSelect: () -> Unit,
) {
    val currencyDetail = CurrencyInfo.getCurrencyDetail(currencyCode)
    DropdownMenuItem(
        onClick = onSelect,
        leadingIcon = {
            currencyDetail?.let { detail ->
                Image(
                    painter = painterResource(detail.flagResId),
                    contentDescription = detail.fullName,
                    modifier = Modifier.size(MaterialTheme.spacing.large),
                )
            }
        },
        text = { Text(text = currencyCode) },
    )
}
