package me.mitkovic.kmp.currencyconverter.navigation

import androidx.compose.runtime.Composable
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
    currentScreen: Screen,
    navigate: (Screen) -> Unit,
    onAction: (MainAction) -> Unit,
) {
    when (currentScreen) {
        is Screen.Converter -> {
            val converterViewModel: ConverterViewModel = koinInject<ConverterViewModel>()
            ConverterScreen(
                viewModel = converterViewModel,
                onNavigateToFavorites = { navigate(Screen.Favorites) },
            )

            onAction(MainAction.TitleTextChanged(stringResource(Res.string.app_name)))
            onAction(MainAction.ShowActionsChanged(true))
            onAction(MainAction.ShowBackIconChanged(false))
            onAction(MainAction.ShowReloadButtonChanged(true))
        }
        is Screen.Favorites -> {
            val favoritesViewModel: FavoritesViewModel = koinInject<FavoritesViewModel>()
            FavoritesScreen(
                viewModel = favoritesViewModel,
                onNavigateToConverter = { navigate(Screen.Converter) },
            )

            onAction(MainAction.TitleTextChanged(stringResource(Res.string.converter_favorites)))
            onAction(MainAction.ShowActionsChanged(false))
            onAction(MainAction.ShowBackIconChanged(true))
            onAction(MainAction.ShowReloadButtonChanged(false))
        }
    }
}
