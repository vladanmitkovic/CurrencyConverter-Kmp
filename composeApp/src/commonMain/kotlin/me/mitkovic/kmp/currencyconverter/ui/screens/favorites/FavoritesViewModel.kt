package me.mitkovic.kmp.currencyconverter.ui.screens.favorites

import androidx.lifecycle.ViewModel
import me.mitkovic.kmp.currencyconverter.logging.AppLogger

class FavoritesViewModel(
    logger: AppLogger,
) : ViewModel() {

    init {
        logger.logError(FavoritesViewModel::class.simpleName, "FavoritesViewModel", null)
        loadFavorites()
    }

    fun loadFavorites() {
    }
}
