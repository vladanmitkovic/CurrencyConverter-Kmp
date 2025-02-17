package me.mitkovic.kmp.currencyconverter.ui.screens.converter

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

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

    Text(state.rates.rates.toString())

    // Replace this with your actual converter UI
    Button(onClick = onNavigateToFavorites) {
        Text("Go to Favorites")
    }
}
