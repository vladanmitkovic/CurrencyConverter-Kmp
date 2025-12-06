package me.mitkovic.kmp.currencyconverter.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {

    @Serializable
    object Converter : Screen

    @Serializable
    object Favorites : Screen
}
