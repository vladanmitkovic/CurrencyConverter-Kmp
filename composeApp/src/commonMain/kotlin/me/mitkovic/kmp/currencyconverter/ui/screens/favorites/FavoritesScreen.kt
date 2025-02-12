package me.mitkovic.kmp.currencyconverter.ui.screens.favorites

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel,
    onNavigateToConverter: () -> Unit,
) {
    // Replace this with your actual favorites UI
    Button(onClick = onNavigateToConverter) {
        Text("Go to Converter")
    }
}
