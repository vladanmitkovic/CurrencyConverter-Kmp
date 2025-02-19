package me.mitkovic.kmp.currencyconverter.ui.screens.converter

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.mitkovic.kmp.currencyconverter.common.Constants
import me.mitkovic.kmp.currencyconverter.platform.formatNumber
import me.mitkovic.kmp.currencyconverter.ui.theme.spacing

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
