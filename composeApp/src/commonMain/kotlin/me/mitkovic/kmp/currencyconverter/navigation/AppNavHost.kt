package me.mitkovic.kmp.currencyconverter.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import me.mitkovic.kmp.currencyconverter.common.ConnectivityObserver
import me.mitkovic.kmp.currencyconverter.ui.screens.converter.ConverterScreen
import me.mitkovic.kmp.currencyconverter.ui.screens.converter.ConverterViewModel
import me.mitkovic.kmp.currencyconverter.ui.screens.favorites.FavoritesScreen
import me.mitkovic.kmp.currencyconverter.ui.screens.favorites.FavoritesViewModel
import org.koin.compose.koinInject

@Composable
fun AppNavHost(
    navHostController: NavController,
    networkStatus: () -> ConnectivityObserver.Status?,
    onThemeClick: () -> Unit,
) {

    NavHost(
        navController = navHostController as NavHostController,
        startDestination = Screen.Converter,
    ) {

        // CurrencyConverter screen
        composable<Screen.Converter> {
            val converterViewModel: ConverterViewModel = koinInject<ConverterViewModel>()
            ConverterScreen(
                viewModel = converterViewModel,
                networkStatus = networkStatus,
                onFavoritesClick = {
                    navHostController.navigate(
                        Screen.Favorites,
                    )
                },
                onThemeClick = onThemeClick,
            )

        }

        // Favorites screen
        composable<Screen.Favorites> {
            val favoritesViewModel: FavoritesViewModel = koinInject<FavoritesViewModel>()
            FavoritesScreen(
                viewModel = favoritesViewModel,
                networkStatus = networkStatus,
                onBackClick = {
                    navHostController.navigate(
                        Screen.Converter,
                    )
                },
            )

        }
    }

}
