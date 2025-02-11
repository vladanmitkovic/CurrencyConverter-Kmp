package me.mitkovic.kmp.currencyconverter.navigation

sealed class Screen {

    object Converter : Screen()

    object Favorites : Screen()
}
