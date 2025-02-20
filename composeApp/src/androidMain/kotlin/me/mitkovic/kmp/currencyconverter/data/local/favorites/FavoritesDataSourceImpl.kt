package me.mitkovic.kmp.currencyconverter.data.local.favorites

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.mitkovic.kmp.currencyconverter.common.Constants

class FavoritesDataSourceImpl(
    private val dataStore: DataStore<Preferences>,
) : FavoritesDataSource {

    companion object {
        private val FAVORITE_CURRENCIES_KEY = stringPreferencesKey("favorite_currencies")
    }

    override fun getFavoriteCurrencies(): Flow<List<String>> =
        dataStore.data.map { preferences ->
            preferences[FAVORITE_CURRENCIES_KEY]?.split(",") ?: Constants.PREFERRED_FAVORITES
        }

    override suspend fun updateFavoriteCurrencies(favorites: List<String>) {
        dataStore.edit { preferences ->
            preferences[FAVORITE_CURRENCIES_KEY] = favorites.joinToString(",")
        }
    }
}
