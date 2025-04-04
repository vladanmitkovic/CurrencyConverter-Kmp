package me.mitkovic.kmp.currencyconverter.ui.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
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
    navHostController: NavController,
    networkStatusIndicator: @Composable (SnackbarHostState, Boolean, () -> Unit) -> Unit,
    snackbarHostState: SnackbarHostState,
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
                networkStatusIndicator = networkStatusIndicator,
                snackbarHostState = snackbarHostState,
                showReloadButton = true,
                onReload = { converterViewModel.refreshConversionRates() },
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
                networkStatusIndicator = networkStatusIndicator,
                snackbarHostState = snackbarHostState,
                showReloadButton = false,
                onReload = { },
                onBackClick = {
                    navHostController.navigate(
                        Screen.Converter,
                    )
                },
            )
        }
    }
}
