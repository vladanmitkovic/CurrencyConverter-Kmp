package me.mitkovic.kmp.currencyconverter.ui.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import currencyconverter_kmp.composeapp.generated.resources.Res
import currencyconverter_kmp.composeapp.generated.resources.app_icon
import currencyconverter_kmp.composeapp.generated.resources.content_description_application_icon
import me.mitkovic.kmp.currencyconverter.ui.theme.spacing
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ApplicationTitle(
    title: String,
    showActions: Boolean,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showActions) {
            Icon(
                painter = painterResource(Res.drawable.app_icon),
                contentDescription = stringResource(Res.string.content_description_application_icon),
                modifier = Modifier.size(MaterialTheme.spacing.iconSize),
                tint = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
        }
        Text(
            text = title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.headlineLarge.copy(color = MaterialTheme.colorScheme.onBackground),
        )
    }
}
