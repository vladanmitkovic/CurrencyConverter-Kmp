package me.mitkovic.kmp.currencyconverter.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.mitkovic.kmp.currencyconverter.common.Constants
import me.mitkovic.kmp.currencyconverter.data.repository.CurrencyConverterRepository
import me.mitkovic.kmp.currencyconverter.logging.AppLogger

class FavoritesViewModel(
    private val currencyConverterRepository: CurrencyConverterRepository,
    private val logger: AppLogger,
) : ViewModel() {

    val favorites: StateFlow<List<String>> =
        currencyConverterRepository.favoritesRepository
            .getFavoriteCurrencies()
            .onEach { favoritesList ->
                logger.logDebug(FavoritesViewModel::class.simpleName, "favoritesList: $favoritesList")
            }.catch { e ->
                logger.logError(FavoritesViewModel::class.simpleName, "Error loading favorites", e)
                emit(emptyList())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = Constants.PREFERRED_FAVORITES,
            )

    fun addFavorite(currency: String) {
        val updatedFavorites =
            favorites.value.toMutableList().apply {
                if (!contains(currency)) add(currency)
            }
        updateFavoritesInRepository(updatedFavorites)
    }

    fun removeFavorite(currency: String) {
        val updatedFavorites =
            favorites.value.toMutableList().apply {
                remove(currency)
            }
        updateFavoritesInRepository(updatedFavorites)
    }

    private fun updateFavoritesInRepository(updatedFavorites: List<String>) {
        viewModelScope.launch {
            currencyConverterRepository.favoritesRepository.updateFavoriteCurrencies(updatedFavorites)
        }
        logger.logDebug(FavoritesViewModel::class.simpleName, "updatedFavorites: $updatedFavorites")
    }
}
