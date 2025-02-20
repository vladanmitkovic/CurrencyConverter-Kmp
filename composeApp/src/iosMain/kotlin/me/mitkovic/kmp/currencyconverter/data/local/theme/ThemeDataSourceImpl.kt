package me.mitkovic.kmp.currencyconverter.data.local.theme

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import platform.Foundation.NSUserDefaults

class ThemeDataSourceImpl(
    private val defaults: NSUserDefaults,
) : ThemeDataSource {

    private val THEME_KEY = "is_dark_mode"
    private val isDarkModeFlow = MutableStateFlow(defaults.boolForKey(THEME_KEY))

    override suspend fun saveTheme(isDarkMode: Boolean) {
        defaults.setBool(isDarkMode, forKey = THEME_KEY)
        isDarkModeFlow.value = isDarkMode
    }

    override fun getTheme(): Flow<Boolean> = isDarkModeFlow
}
