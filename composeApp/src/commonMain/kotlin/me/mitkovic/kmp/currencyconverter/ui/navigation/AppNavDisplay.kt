package me.mitkovic.kmp.currencyconverter.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import me.mitkovic.kmp.currencyconverter.ui.screens.converter.ConverterScreen
import me.mitkovic.kmp.currencyconverter.ui.screens.converter.ConverterViewModel
import me.mitkovic.kmp.currencyconverter.ui.screens.favorites.FavoritesScreen
import me.mitkovic.kmp.currencyconverter.ui.screens.favorites.FavoritesViewModel
import org.koin.compose.koinInject

@Composable
fun AppNavDisplay(
    navigator: AppNavigator,
    setOnReload: ((() -> Unit)?) -> Unit,
    onError: (String) -> Unit,
) {
    NavDisplay(
        backStack = navigator.backStack,
        onBack = { navigator.goBack() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {

            // CurrencyConverter screen
            entry<Screen.Converter> {
                val converterViewModel: ConverterViewModel = koinInject()
                ConverterScreen(
                    viewModel = converterViewModel,
                    setOnReload = setOnReload,
                    onError = onError,
                )
            }

            // Favorites screen
            entry<Screen.Favorites> {
                val favoritesViewModel: FavoritesViewModel = koinInject()
                FavoritesScreen(viewModel = favoritesViewModel)
            }
        },
    )
}
