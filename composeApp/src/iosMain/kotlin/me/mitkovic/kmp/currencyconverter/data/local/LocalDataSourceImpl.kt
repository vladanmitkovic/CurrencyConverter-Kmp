package me.mitkovic.kmp.currencyconverter.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import platform.Foundation.NSUserDefaults

class LocalDataSourceImpl : LocalDataSource {

    private val defaults = NSUserDefaults.standardUserDefaults()

    private val THEME_KEY = "is_dark_mode"

    private val isDarkModeFlow = MutableStateFlow(defaults.boolForKey(THEME_KEY))

    override fun getTheme(): Flow<Boolean> = isDarkModeFlow

    override suspend fun saveTheme(isDarkMode: Boolean) {
        defaults.setBool(isDarkMode, forKey = THEME_KEY)
        isDarkModeFlow.value = isDarkMode
    }
}
