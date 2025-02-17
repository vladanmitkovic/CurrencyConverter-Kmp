package me.mitkovic.kmp.currencyconverter.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.json.Json
import me.mitkovic.kmp.currencyconverter.data.local.database.CurrencyConverterDatabase
import platform.Foundation.NSUserDefaults

class LocalDataSourceImpl(
    private val defaults: NSUserDefaults,
    database: CurrencyConverterDatabase,
    json: Json,
) : SharedLocalDataSource(database, json) {

    private val THEME_KEY = "is_dark_mode"

    private val isDarkModeFlow = MutableStateFlow(defaults.boolForKey(THEME_KEY))

    override fun getTheme(): Flow<Boolean> = isDarkModeFlow

    override suspend fun saveTheme(isDarkMode: Boolean) {
        defaults.setBool(isDarkMode, forKey = THEME_KEY)
        isDarkModeFlow.value = isDarkMode
    }
}
