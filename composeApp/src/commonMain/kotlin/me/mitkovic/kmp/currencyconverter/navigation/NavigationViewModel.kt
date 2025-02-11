package me.mitkovic.kmp.currencyconverter.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class NavigationViewModel {

    var currentScreen: Screen by mutableStateOf(Screen.Converter)

    fun navigateTo(screen: Screen) {
        currentScreen = screen
    }
}
