package me.mitkovic.kmp.currencyconverter.data.local.theme

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThemeDataSourceImpl(
    private val dataStore: DataStore<Preferences>,
) : IThemeDataSource {

    companion object {
        private val THEME_KEY = booleanPreferencesKey("is_light_mode")
    }

    override suspend fun saveTheme(isLightMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = isLightMode
        }
    }

    override fun getTheme(): Flow<Boolean> =
        dataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: false
        }
}
