package me.mitkovic.kmp.currencyconverter.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack

class AppNavigator internal constructor(
    val backStack: NavBackStack<NavKey>,
) {
    fun navigate(route: Screen) {
        backStack.add(route)
    }

    fun goBack() {
        if (backStack.size > 1) {
            backStack.removeLastOrNull()
        }
    }
}

@Composable
fun rememberAppNavigator(): AppNavigator {
    val backStack: NavBackStack<NavKey> = rememberNavBackStack(
        NavSavedStateConfiguration,
        Screen.Converter,
    )
    return remember(backStack) { AppNavigator(backStack) }
}
