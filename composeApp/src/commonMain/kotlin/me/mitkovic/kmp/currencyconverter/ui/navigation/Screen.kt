package me.mitkovic.kmp.currencyconverter.ui.navigation

import kotlinx.serialization.Serializable

sealed class Screen {

    @Serializable
    object Converter : Screen()

    @Serializable
    object Favorites : Screen()
}
