package me.mitkovic.kmp.currencyconverter.ui.screens.converter

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ConverterScreen(
    viewModel: ConverterViewModel,
    onNavigateToFavorites: () -> Unit,
) {
    val uiState = viewModel.conversionRatesUiState.collectAsStateWithLifecycle()
    val state = uiState.value

    viewModel.logMessage(state.toString())

    Text(state.rates.rates.toString())

    // Replace this with your actual converter UI
    Button(onClick = onNavigateToFavorites) {
        Text("Go to Favorites")
    }
}
