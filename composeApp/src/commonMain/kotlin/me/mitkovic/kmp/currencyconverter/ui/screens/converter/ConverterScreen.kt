package me.mitkovic.kmp.currencyconverter.ui.screens.converter

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import currencyconverter_kmp.composeapp.generated.resources.Res
import currencyconverter_kmp.composeapp.generated.resources.content_description_swap_currencies
import currencyconverter_kmp.composeapp.generated.resources.swap_horizontally
import currencyconverter_kmp.composeapp.generated.resources.unknown
import me.mitkovic.kmp.currencyconverter.platform.formatDateTime
import me.mitkovic.kmp.currencyconverter.platform.formatNumber
import me.mitkovic.kmp.currencyconverter.ui.common.CurrencyWithFlag
import me.mitkovic.kmp.currencyconverter.ui.common.CustomDropdownMenuItem
import me.mitkovic.kmp.currencyconverter.ui.common.FullCurrencyName
import me.mitkovic.kmp.currencyconverter.ui.common.NumericKeypad
import me.mitkovic.kmp.currencyconverter.ui.common.ProgressIndicator
import me.mitkovic.kmp.currencyconverter.ui.common.Ticker
import me.mitkovic.kmp.currencyconverter.ui.theme.spacing
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ConverterScreen(
    viewModel: ConverterViewModel,
    onError: (String) -> Unit,
    setOnReload: ((() -> Unit)?) -> Unit,
) {
    val uiState = viewModel.conversionRatesUiState.collectAsStateWithLifecycle()
    val state = uiState.value

    LaunchedEffect(state) {
        viewModel.logMessage("state: $state")
    }

    val refreshState by viewModel.refreshRatesUiState.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        setOnReload { viewModel.refreshConversionRates() }
        onDispose {
            setOnReload(null)
        }
    }

    // Error handling in UI (simpler and avoids race conditions)
    var lastError by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(state, refreshState) {
        val error =
            when {
                state is ConversionRatesUiState.Error -> state.error
                refreshState is ConversionRatesUiState.Error -> (refreshState as ConversionRatesUiState.Error).error
                else -> null
            }
        if (error != null && error != lastError) {
            onError(error)
            lastError = error
        }
    }

    // Collecting the favorites list as state
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()

    // Using ViewModel's orderedCurrencies
    val allCurrencies by viewModel.orderedCurrencies.collectAsStateWithLifecycle()

    val selectedCurrencyLeft by viewModel.selectedCurrencyLeft.collectAsState(initial = "")
    val selectedCurrencyRight by viewModel.selectedCurrencyRight.collectAsState(initial = "")

    // Using ViewModel's rates flow
    val rates by viewModel.rates.collectAsStateWithLifecycle()

    // Using ViewModel's computed states
    val amount by viewModel.amount.collectAsStateWithLifecycle()
    val convertedAmount by viewModel.convertedAmount.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()

    Column(
        modifier =
            Modifier
                .fillMaxSize(),
    ) {
        Column(
            modifier =
                Modifier
                    .padding(MaterialTheme.spacing.medium)
                    .weight(1f, fill = true)
                    .verticalScroll(scrollState),
        ) {
            // Currencies Dropdown
            CurrencySelectionRow(
                rates = allCurrencies,
                favorites = favorites,
                selectedCurrencyLeft = selectedCurrencyLeft,
                onCurrencyLeftSelected = viewModel::setSelectedCurrencyLeft,
                selectedCurrencyRight = selectedCurrencyRight,
                onCurrencyRightSelected = viewModel::setSelectedCurrencyRight,
                onSwapCurrencies = {
                    viewModel.swapCurrencies()
                },
            )

            val ratesTimestamp: Long? =
                when (state) {
                    is ConversionRatesUiState.Success -> state.timestamp
                    else -> null
                }

            // Display for the amount and its conversion
            AmountAndConversionDisplay(
                ratesTimestamp = ratesTimestamp,
                currencyFrom = selectedCurrencyLeft,
                amount = amount,
                currencyTo = selectedCurrencyRight,
                convertedAmount = convertedAmount,
            )

            // CHANGED: Using ViewModel methods for keypad actions
            NumericKeypad(
                onNumberClick = viewModel::onNumberClick,
                onDecimalClick = viewModel::onDecimalClick,
                onDeleteClick = viewModel::onDeleteClick,
            )
        }

        ProgressIndicator(refreshState = refreshState)

        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.surfaceVariant,
        )

        // Ticker holder
        Ticker(
            rates = rates,
            selectedCurrencyLeft = selectedCurrencyLeft,
        )
    }
}

@Composable
fun CurrencySelectionRow(
    rates: List<String>,
    favorites: List<String>,
    selectedCurrencyLeft: String,
    onCurrencyLeftSelected: (String) -> Unit,
    selectedCurrencyRight: String,
    onCurrencyRightSelected: (String) -> Unit,
    onSwapCurrencies: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxSize()
                .height(MaterialTheme.spacing.xxLarge)
                .padding(bottom = MaterialTheme.spacing.medium),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        CurrencySelectionDropdown(
            selectedCurrency = selectedCurrencyLeft,
            currencies = rates,
            favorites = favorites,
            onCurrencySelected = onCurrencyLeftSelected,
        )

        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onSwapCurrencies() },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(Res.drawable.swap_horizontally),
                contentDescription = stringResource(Res.string.content_description_swap_currencies),
            )
        }

        CurrencySelectionDropdown(
            selectedCurrency = selectedCurrencyRight,
            currencies = rates,
            favorites = favorites,
            onCurrencySelected = onCurrencyRightSelected,
        )
    }
}

@Composable
fun CurrencySelectionDropdown(
    selectedCurrency: String,
    currencies: List<String>,
    favorites: List<String>,
    onCurrencySelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    // Separating currencies into favorites and others
    val favoriteCurrencies = currencies.filter { it in favorites }
    val otherCurrencies = currencies.filterNot { it in favorites }

    Box {
        OutlinedButton(
            onClick = { expanded = true },
            modifier =
                Modifier
                    .clip(RoundedCornerShape(MaterialTheme.spacing.extraMedium))
                    .height(MaterialTheme.spacing.currencySelection)
                    .width(MaterialTheme.spacing.xxLarge),
            shape = RoundedCornerShape(MaterialTheme.spacing.extraMedium),
            contentPadding = PaddingValues(MaterialTheme.spacing.small),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)),
            colors =
                ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
        ) {
            CurrencyWithFlag(selectedCurrency)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
        ) {
            // Display favorites with a title if there are any
            if (favoriteCurrencies.isNotEmpty()) {
                DropdownMenuItem(
                    text = { Text("Favorites") },
                    enabled = false,
                    onClick = {},
                )
                favoriteCurrencies.forEach { currency ->
                    CustomDropdownMenuItem(currencyCode = currency, onSelect = {
                        onCurrencySelected(currency)
                        expanded = false
                    })
                }
            }

            // Separator or title for others
            if (favoriteCurrencies.isNotEmpty() && otherCurrencies.isNotEmpty()) {
                DropdownMenuItem(
                    text = { Text("Others") },
                    enabled = false,
                    onClick = {},
                )
            }

            // Display other currencies
            otherCurrencies.forEach { currency ->
                CustomDropdownMenuItem(currencyCode = currency, onSelect = {
                    onCurrencySelected(currency)
                    expanded = false
                })
            }
        }
    }
}

@Composable
fun AmountAndConversionDisplay(
    ratesTimestamp: Long?,
    currencyFrom: String,
    amount: Double,
    currencyTo: String,
    convertedAmount: Double,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(bottom = MaterialTheme.spacing.medium),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        // Display the amount with increased font size and bold
        Column {
            FullCurrencyName(currencyCode = currencyFrom)
            Text(
                text = amount.toString(),
                modifier = Modifier.align(Alignment.Start),
                style =
                    MaterialTheme
                        .typography
                        .headlineLarge
                        .copy(
                            color = MaterialTheme.colorScheme.onBackground,
                        ),
            )
        }

        // Display the converted amount with increased font size and bold
        Column {
            FullCurrencyName(modifier = Modifier.align(Alignment.End), currencyCode = currencyTo)
            Text(
                text = formatNumber(convertedAmount, 2),
                modifier = Modifier.align(Alignment.End),
                style =
                    MaterialTheme
                        .typography
                        .headlineLarge
                        .copy(
                            color = MaterialTheme.colorScheme.onBackground,
                        ),
            )
            Text(
                text =
                    ratesTimestamp?.let {
                        formatDateTime(ratesTimestamp * 1000)
                    } ?: stringResource(
                        Res.string.unknown,
                    ),
                style = MaterialTheme.typography.labelSmall,
                modifier =
                    Modifier
                        .align(Alignment.End)
                        .padding(bottom = MaterialTheme.spacing.extraSmall),
            )
        }
    }
}
