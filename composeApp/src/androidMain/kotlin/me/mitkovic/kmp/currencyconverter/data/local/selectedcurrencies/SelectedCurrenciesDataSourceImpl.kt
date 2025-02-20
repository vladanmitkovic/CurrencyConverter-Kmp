package me.mitkovic.kmp.currencyconverter.data.local.selectedcurrencies

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.mitkovic.kmp.currencyconverter.common.Constants

class SelectedCurrenciesDataSourceImpl(
    private val dataStore: DataStore<Preferences>,
) : SelectedCurrenciesDataSource {

    companion object {
        val SELECTED_CURRENCY_LEFT_KEY = stringPreferencesKey("selected_currency_left")
        val SELECTED_CURRENCY_RIGHT_KEY = stringPreferencesKey("selected_currency_right")
    }

    override suspend fun setSelectedCurrencyLeft(currencyCode: String) {
        dataStore.edit { preferences ->
            preferences[SELECTED_CURRENCY_LEFT_KEY] = currencyCode
        }
    }

    override fun getSelectedCurrencyLeft(): Flow<String> =
        dataStore.data
            .map { preferences ->
                preferences[SELECTED_CURRENCY_LEFT_KEY] ?: Constants.PREFERRED_FAVORITES[0]
            }

    override suspend fun setSelectedCurrencyRight(currencyCode: String) {
        dataStore.edit { preferences ->
            preferences[SELECTED_CURRENCY_RIGHT_KEY] = currencyCode
        }
    }

    override fun getSelectedCurrencyRight(): Flow<String> =
        dataStore.data
            .map { preferences ->
                preferences[SELECTED_CURRENCY_RIGHT_KEY] ?: Constants.PREFERRED_FAVORITES[1]
            }
}
