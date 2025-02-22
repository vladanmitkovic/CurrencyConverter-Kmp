package me.mitkovic.kmp.currencyconverter.ui.screens.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import currencyconverter_kmp.composeapp.generated.resources.Res
import currencyconverter_kmp.composeapp.generated.resources.add
import currencyconverter_kmp.composeapp.generated.resources.add_circle
import currencyconverter_kmp.composeapp.generated.resources.add_to_favorites
import currencyconverter_kmp.composeapp.generated.resources.remove
import currencyconverter_kmp.composeapp.generated.resources.remove_circle
import currencyconverter_kmp.composeapp.generated.resources.your_favorites
import me.mitkovic.kmp.currencyconverter.common.Constants.PREFERRED_CURRENCY_ORDER
import me.mitkovic.kmp.currencyconverter.ui.theme.spacing
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun FavoritesScreen(viewModel: FavoritesViewModel) {
    // Collecting the favorites list as state
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()

    // Exclude favorite currencies from PREFERRED_CURRENCY_ORDER
    val nonFavoriteCurrencies = PREFERRED_CURRENCY_ORDER.filterNot { it in favorites }

    val spacing = MaterialTheme.spacing
    Column(
        modifier =
            Modifier
                .background(
                    color = MaterialTheme.colorScheme.background,
                ).padding(horizontal = spacing.medium)
                .fillMaxSize(),
    ) {
        LazyColumn {
            // Displaying the list of favorite currencies
            item {
                Text(
                    text = stringResource(Res.string.your_favorites),
                    modifier = Modifier.padding(spacing.medium),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            items(favorites) { currency ->

                CurrencyItem(
                    currency = currency,
                    action = stringResource(Res.string.remove),
                    onAddRemoveAction = { favorite -> viewModel.removeFavorite(favorite) },
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                )
            }

            // Spacer for visual separation
            item { Spacer(modifier = Modifier.height(spacing.medium)) }

            // Header for non-favorites section
            item {
                Text(
                    text = stringResource(Res.string.add_to_favorites),
                    modifier = Modifier.padding(spacing.medium),
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            // Displaying the list of non-favorite currencies for adding to favorites
            items(nonFavoriteCurrencies) { currency ->

                CurrencyItem(
                    currency = currency,
                    action = stringResource(Res.string.add),
                    onAddRemoveAction = { favorite -> viewModel.addFavorite(favorite) },
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        }
    }
}

@Composable
fun CurrencyItem(
    currency: String,
    action: String,
    onAddRemoveAction: (String) -> Unit,
) {
    // Here we are using theme spacing for padding and height values
    val spacing = MaterialTheme.spacing
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = spacing.medium, vertical = spacing.small),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = currency,
            modifier = Modifier.align(Alignment.CenterVertically),
            style = MaterialTheme.typography.bodySmall,
        )

        val painterResource: Painter =
            if (action.equals(stringResource(Res.string.add))) {
                painterResource(Res.drawable.add_circle)
            } else {
                painterResource(Res.drawable.remove_circle)
            }

        IconButton(
            onClick = {
                onAddRemoveAction(currency)
            },
        ) {
            Icon(
                painter = painterResource,
                modifier =
                    Modifier
                        .size(spacing.iconSize),
                contentDescription = action,
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}
