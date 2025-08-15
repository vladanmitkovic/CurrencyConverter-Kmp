package me.mitkovic.kmp.currencyconverter.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
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
fun NavController.currentTopBarState(): TopBarState {
    val currentBackStackEntry by currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination
    val isHome = currentDestination?.hasRoute(Screen.Converter::class) ?: true
    return TopBarState(
        title = if (isHome) stringResource(Res.string.app_name) else stringResource(Res.string.converter_favorites),
        showActions = isHome,
        showBackIcon = !isHome,
        isHome = isHome,
    )
}
