package me.mitkovic.kmp.currencyconverter.data.local.theme

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import platform.Foundation.NSUserDefaults

class ThemeDataSourceImpl(
    private val defaults: NSUserDefaults,
) : ThemeDataSource {

    private val THEME_KEY = "is_dark_mode"
    private val isLightModeFlow = MutableStateFlow(defaults.boolForKey(THEME_KEY))

    override suspend fun saveTheme(isLightMode: Boolean) {
        defaults.setBool(isLightMode, forKey = THEME_KEY)
        isLightModeFlow.value = isLightMode
    }

    override fun getTheme(): Flow<Boolean> = isLightModeFlow
}
