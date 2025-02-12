package me.mitkovic.kmp.currencyconverter.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalDataSourceImpl(
    context: Context,
) : LocalDataSource {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")
    private val dataStore = context.dataStore

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
