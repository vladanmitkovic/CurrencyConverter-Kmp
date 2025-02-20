package me.mitkovic.kmp.currencyconverter.data.local.theme

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class ThemeDataSourceImpl : ThemeDataSource {

    private val isDarkModeFlow = MutableStateFlow(false)

    override suspend fun saveTheme(isDarkMode: Boolean) {
        isDarkModeFlow.value = isDarkMode
    }

    override fun getTheme(): Flow<Boolean> = isDarkModeFlow
}
