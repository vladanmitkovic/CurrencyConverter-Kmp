package me.mitkovic.kmp.currencyconverter.ui.screens.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import currencyconverter_kmp.composeapp.generated.resources.Res
import currencyconverter_kmp.composeapp.generated.resources.add
import currencyconverter_kmp.composeapp.generated.resources.add_to_favorites
import currencyconverter_kmp.composeapp.generated.resources.remove
import currencyconverter_kmp.composeapp.generated.resources.your_favorites
import me.mitkovic.kmp.currencyconverter.common.Constants.PREFERRED_CURRENCY_ORDER
import me.mitkovic.kmp.currencyconverter.ui.common.StyledTextButton
import me.mitkovic.kmp.currencyconverter.ui.theme.spacing
import org.jetbrains.compose.resources.stringResource

@Composable
fun FavoritesScreen(viewModel: FavoritesViewModel) {
    // Collecting the favorites list as state
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()

    // Exclude favorite currencies from PREFERRED_CURRENCY_ORDER
    val nonFavoriteCurrencies = PREFERRED_CURRENCY_ORDER.filterNot { it in favorites }

    Column(
        modifier =
            Modifier
                .padding(horizontal = MaterialTheme.spacing.medium)
                .fillMaxSize(),
    ) {
        LazyColumn {
            // Displaying the list of favorite currencies
            item {
                Text(
                    text = stringResource(Res.string.your_favorites),
                    modifier = Modifier.padding(MaterialTheme.spacing.medium),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            items(favorites) { currency ->

                val removeFavorite = viewModel::removeFavorite

                CurrencyItem(
                    currency = currency,
                    action = stringResource(Res.string.remove),
                    onAddRemoveAction = { favorite -> removeFavorite(favorite) },
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                )
            }

            // Spacer for visual separation
            item { Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium)) }

            // Header for non-favorites section
            item {
                Text(
                    text = stringResource(Res.string.add_to_favorites),
                    modifier = Modifier.padding(MaterialTheme.spacing.medium),
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            // Displaying the list of non-favorite currencies for adding to favorites
            items(nonFavoriteCurrencies) { currency ->

                val addFavorite = viewModel::addFavorite

                CurrencyItem(
                    currency = currency,
                    action = stringResource(Res.string.add),
                    onAddRemoveAction = { favorite -> addFavorite(favorite) },
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

        StyledTextButton(
            buttonText = action, // "Add" or "Remove"
            modifier =
                Modifier
                    .height(spacing.largeItem),
            textStyle = MaterialTheme.typography.labelSmall,
            onClick = { onAddRemoveAction(currency) },
        )
    }
}
