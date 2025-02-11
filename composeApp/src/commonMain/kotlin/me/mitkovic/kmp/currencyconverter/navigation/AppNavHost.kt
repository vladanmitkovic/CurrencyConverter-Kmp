package me.mitkovic.kmp.currencyconverter.navigation

import androidx.compose.runtime.Composable
import me.mitkovic.kmp.currencyconverter.ui.screens.converter.ConverterScreen
import me.mitkovic.kmp.currencyconverter.ui.screens.converter.ConverterViewModel
import me.mitkovic.kmp.currencyconverter.ui.screens.favorites.FavoritesScreen
import me.mitkovic.kmp.currencyconverter.ui.screens.favorites.FavoritesViewModel
import org.koin.compose.koinInject

@Composable
fun AppNavHost(
    currentScreen: Screen,
    navigate: (Screen) -> Unit,
) {
    when (currentScreen) {
        is Screen.Converter -> {
            val converterViewModel: ConverterViewModel = koinInject<ConverterViewModel>()
            ConverterScreen(
                viewModel = converterViewModel,
                onNavigateToFavorites = { navigate(Screen.Favorites) },
            )
        }
        is Screen.Favorites -> {
            val favoritesViewModel: FavoritesViewModel = koinInject<FavoritesViewModel>()
            FavoritesScreen(
                viewModel = favoritesViewModel,
                onNavigateToConverter = { navigate(Screen.Converter) },
            )
        }
    }
}
