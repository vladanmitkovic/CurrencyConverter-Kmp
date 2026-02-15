package me.mitkovic.kmp.currencyconverter.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import currencyconverter_kmp.composeapp.generated.resources.Res
import currencyconverter_kmp.composeapp.generated.resources.app_name
import currencyconverter_kmp.composeapp.generated.resources.converter_favorites
import org.jetbrains.compose.resources.stringResource

data class TopBarState(
    val title: String,
    val showActions: Boolean,
    val showBackIcon: Boolean,
    val isHome: Boolean,
)

@Composable
fun currentTopBarState(backStack: NavBackStack<NavKey>): TopBarState {
    val isHome = backStack.lastOrNull() == Screen.Converter
    return TopBarState(
        title = if (isHome) stringResource(Res.string.app_name) else stringResource(Res.string.converter_favorites),
        showActions = isHome,
        showBackIcon = !isHome,
        isHome = isHome,
    )
}
