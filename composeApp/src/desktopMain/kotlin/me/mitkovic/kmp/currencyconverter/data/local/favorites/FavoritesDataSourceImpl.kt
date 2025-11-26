package me.mitkovic.kmp.currencyconverter.data.local.favorites

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import me.mitkovic.kmp.currencyconverter.common.Constants
import java.util.prefs.Preferences

class FavoritesDataSourceImpl : IFavoritesDataSource {

    private val prefs = Preferences.userNodeForPackage(FavoritesDataSourceImpl::class.java)

    companion object {
        private const val FAVORITE_CURRENCIES_KEY = "favorite_currencies"
    }

    private val favoritesFlow = MutableStateFlow(loadFavorites())

    private fun loadFavorites(): List<String> {
        val stored = prefs.get(FAVORITE_CURRENCIES_KEY, null)
        return stored?.split(",") ?: Constants.PREFERRED_FAVORITES
    }

    override fun getFavoriteCurrencies(): Flow<List<String>> = favoritesFlow

    override suspend fun updateFavoriteCurrencies(favorites: List<String>) {
        prefs.put(FAVORITE_CURRENCIES_KEY, favorites.joinToString(","))
        prefs.flush()
        favoritesFlow.value = favorites
    }
}
