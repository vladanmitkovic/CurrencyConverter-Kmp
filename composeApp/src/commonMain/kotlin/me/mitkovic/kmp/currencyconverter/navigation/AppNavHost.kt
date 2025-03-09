package me.mitkovic.kmp.currencyconverter.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import currencyconverter_kmp.composeapp.generated.resources.Res
import currencyconverter_kmp.composeapp.generated.resources.app_name
import currencyconverter_kmp.composeapp.generated.resources.converter_favorites
import me.mitkovic.kmp.currencyconverter.ui.MainAction
import me.mitkovic.kmp.currencyconverter.ui.screens.converter.ConverterScreen
import me.mitkovic.kmp.currencyconverter.ui.screens.converter.ConverterViewModel
import me.mitkovic.kmp.currencyconverter.ui.screens.favorites.FavoritesScreen
import me.mitkovic.kmp.currencyconverter.ui.screens.favorites.FavoritesViewModel
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun AppNavHost(
    navHostController: NavController,
    refreshTrigger: () -> Boolean,
    onRefreshDone: () -> Unit,
    onAction: (MainAction) -> Unit,
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
                refreshTrigger = refreshTrigger,
                onRefreshDone = onRefreshDone,
            )

            onAction(MainAction.TitleTextChanged(stringResource(Res.string.app_name)))
            onAction(MainAction.ShowActionsChanged(true))
            onAction(MainAction.ShowBackIconChanged(false))
            onAction(MainAction.ShowReloadButtonChanged(true))
        }

        // Favorites screen
        composable<Screen.Favorites> {
            val favoritesViewModel: FavoritesViewModel = koinInject<FavoritesViewModel>()
            FavoritesScreen(
                viewModel = favoritesViewModel,
            )

            onAction(MainAction.TitleTextChanged(stringResource(Res.string.converter_favorites)))
            onAction(MainAction.ShowActionsChanged(false))
            onAction(MainAction.ShowBackIconChanged(true))
            onAction(MainAction.ShowReloadButtonChanged(false))
        }
    }

}
