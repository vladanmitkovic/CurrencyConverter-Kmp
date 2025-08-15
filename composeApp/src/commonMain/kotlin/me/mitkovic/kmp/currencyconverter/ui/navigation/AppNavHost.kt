package me.mitkovic.kmp.currencyconverter.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import me.mitkovic.kmp.currencyconverter.ui.screens.converter.ConverterScreen
import me.mitkovic.kmp.currencyconverter.ui.screens.converter.ConverterViewModel
import me.mitkovic.kmp.currencyconverter.ui.screens.favorites.FavoritesScreen
import me.mitkovic.kmp.currencyconverter.ui.screens.favorites.FavoritesViewModel
import org.koin.compose.koinInject

@Composable
fun AppNavHost(
    navHostController: NavHostController,
    setOnReload: ((() -> Unit)?) -> Unit,
    onError: (String) -> Unit,
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Converter,
    ) {
        // CurrencyConverter screen
        composable<Screen.Converter> {
            val converterViewModel: ConverterViewModel = koinInject<ConverterViewModel>()
            ConverterScreen(
                viewModel = converterViewModel,
                setOnReload = setOnReload,
                onError = onError,
            )
        }

        // Favorites screen
        composable<Screen.Favorites> {
            val favoritesViewModel: FavoritesViewModel = koinInject<FavoritesViewModel>()
            FavoritesScreen(
                viewModel = favoritesViewModel,
            )
        }
    }
}
