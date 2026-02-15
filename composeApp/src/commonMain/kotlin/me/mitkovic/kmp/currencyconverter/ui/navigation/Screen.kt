package me.mitkovic.kmp.currencyconverter.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Screen : NavKey {
    @Serializable data object Converter : Screen
    @Serializable data object Favorites : Screen
}
