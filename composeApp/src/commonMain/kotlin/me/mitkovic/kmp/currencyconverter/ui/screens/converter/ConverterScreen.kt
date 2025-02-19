package me.mitkovic.kmp.currencyconverter.ui.screens.converter

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import currencyconverter_kmp.composeapp.generated.resources.Res
import currencyconverter_kmp.composeapp.generated.resources.content_description_swap_currencies
import currencyconverter_kmp.composeapp.generated.resources.no_currency_detail
import currencyconverter_kmp.composeapp.generated.resources.swap_horizontally
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.mitkovic.kmp.currencyconverter.common.Constants
import me.mitkovic.kmp.currencyconverter.platform.formatNumber
import me.mitkovic.kmp.currencyconverter.ui.theme.spacing
import me.mitkovic.kmp.currencyconverter.ui.utils.CurrencyInfo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ConverterScreen(
    viewModel: ConverterViewModel,
    refreshTrigger: () -> Int,
    onNavigateToFavorites: () -> Unit,
) {
    val uiState = viewModel.conversionRatesUiState.collectAsStateWithLifecycle()
    val state = uiState.value

    viewModel.logMessage("state: " + state.toString())
    viewModel.logMessage("state.rates: " + state.rates)

    val refreshState by viewModel.refreshRatesUiState.collectAsStateWithLifecycle()

    viewModel.logMessage("refreshState: " + refreshState.toString())

    // Use refreshTrigger to trigger refresh in the ViewModel
    LaunchedEffect(refreshTrigger()) {
        if (refreshTrigger() >= 1) viewModel.refreshConversionRates(refreshTrigger())
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize(),
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(horizontal = MaterialTheme.spacing.medium)
                        .weight(1f, fill = true),
                // .verticalScroll(scrollState),
            ) {
                Text(state.rates.rates.toString())

                // Replace this with your actual converter UI
                Button(onClick = onNavigateToFavorites) {
                    Text("Go to Favorites")
                }
            }

            LinearProgressIndicator(state = state, refreshState = refreshState)

            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.surfaceVariant,
            )

            // Ticker holder
            Ticker(
                ratesWrapper = state.rates,
                selectedCurrencyLeft = "EUR",
            )
        }
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
    onSwapCurrencies: () -> Unit, // Add an action to swap currencies
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
    favorites: List<String>, // Added this to differentiate between favorites and others
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
                    .clip(RoundedCornerShape(MaterialTheme.spacing.extraMedium)) // Applies rounded corners
                    .height(MaterialTheme.spacing.currencySelection)
                    .width(MaterialTheme.spacing.xxLarge),
            // Sets the height of the button
            shape = RoundedCornerShape(MaterialTheme.spacing.extraMedium), // Specifies the rounded shape of the button
            contentPadding = PaddingValues(MaterialTheme.spacing.small), // Adjusts padding inside the button
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)), // Sets a semi-transparent border
            colors =
                ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary, // Button background color
                    contentColor = MaterialTheme.colorScheme.onPrimary, // Content (text/icon) color
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

@Composable
fun Ticker(
    ratesWrapper: Rates,
    selectedCurrencyLeft: String,
) {
    val rates = ratesWrapper.rates
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
                    append(" ${formatNumber(entry.value)}")
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

@Composable
fun LinearProgressIndicator(
    state: ConversionRatesUiState,
    refreshState: ConversionRatesUiState,
) {
    var currentProgress by remember { mutableFloatStateOf(0f) }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    if ((state.isLoading || refreshState.isLoading) && !loading) {
        loading = true
        LaunchedEffect(Unit) {
            scope.launch {
                loadProgress { progress ->
                    currentProgress = progress
                }
                loading = false
            }
        }
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
    ) {
        if (loading) {
            LinearProgressIndicator(
                progress = { currentProgress },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

/** Iterate the progress value */
suspend fun loadProgress(updateProgress: (Float) -> Unit) {
    for (i in 1..100) {
        updateProgress(i.toFloat() / 100)
        delay(10)
    }
}
