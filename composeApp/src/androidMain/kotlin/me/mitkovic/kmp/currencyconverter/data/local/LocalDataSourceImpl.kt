package me.mitkovic.kmp.currencyconverter.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import me.mitkovic.kmp.currencyconverter.data.local.database.CurrencyConverterDatabase

class LocalDataSourceImpl(
    private val dataStore: DataStore<Preferences>,
    database: CurrencyConverterDatabase,
    json: Json,
) : SharedLocalDataSource(database, json) {

    companion object {
        private val THEME_KEY = booleanPreferencesKey("is_dark_mode")
    }

    override suspend fun saveTheme(isDarkMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDarkMode
        }
    }

    override fun getTheme(): Flow<Boolean> =
        dataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: false
        }
}
