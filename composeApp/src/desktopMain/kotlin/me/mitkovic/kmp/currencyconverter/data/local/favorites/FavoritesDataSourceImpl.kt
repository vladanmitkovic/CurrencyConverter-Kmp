package me.mitkovic.kmp.currencyconverter.data.local.favorites

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import me.mitkovic.kmp.currencyconverter.common.Constants

class FavoritesDataSourceImpl : FavoritesDataSource {

    private val favoritesFlow = MutableStateFlow<List<String>>(Constants.PREFERRED_FAVORITES)

    override fun getFavoriteCurrencies(): Flow<List<String>> = favoritesFlow

    override suspend fun updateFavoriteCurrencies(favorites: List<String>) {
        favoritesFlow.value = favorites
    }
}
