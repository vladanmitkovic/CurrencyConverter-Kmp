package me.mitkovic.kmp.currencyconverter.ui.screens.converter

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ConverterScreen(
    viewModel: ConverterViewModel,
    onNavigateToFavorites: () -> Unit,
) {
    // Replace this with your actual converter UI
    Button(onClick = onNavigateToFavorites) {
        Text("Go to Favorites")
    }
}
