package me.mitkovic.kmp.currencyconverter.data.local.favorites

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import me.mitkovic.kmp.currencyconverter.common.Constants
import platform.Foundation.NSUserDefaults

class FavoritesDataSourceImpl(
    private val defaults: NSUserDefaults,
) : IFavoritesDataSource {

    companion object {
        private const val FAVORITE_CURRENCIES_KEY = "favorite_currencies"
    }

    private val favoritesFlow = MutableStateFlow(Constants.PREFERRED_FAVORITES)

    init {
        defaults.stringForKey(FAVORITE_CURRENCIES_KEY)?.let { stored ->
            favoritesFlow.value = stored.split(",")
        }
    }

    override fun getFavoriteCurrencies(): Flow<List<String>> = favoritesFlow

    override suspend fun updateFavoriteCurrencies(favorites: List<String>) {
        favoritesFlow.value = favorites
        defaults.setObject(favorites.joinToString(","), forKey = FAVORITE_CURRENCIES_KEY)
    }
}
