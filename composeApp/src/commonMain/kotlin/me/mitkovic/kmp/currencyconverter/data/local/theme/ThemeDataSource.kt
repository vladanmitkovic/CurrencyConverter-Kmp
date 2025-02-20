package me.mitkovic.kmp.currencyconverter.data.local.theme

import kotlinx.coroutines.flow.Flow

interface ThemeDataSource {

    suspend fun saveTheme(isDarkMode: Boolean)

    fun getTheme(): Flow<Boolean>
}
