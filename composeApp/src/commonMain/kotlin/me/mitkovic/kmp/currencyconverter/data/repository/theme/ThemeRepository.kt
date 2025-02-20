package me.mitkovic.kmp.currencyconverter.data.repository.theme

import kotlinx.coroutines.flow.Flow

interface ThemeRepository {

    suspend fun saveTheme(isDarkMode: Boolean)

    fun getTheme(): Flow<Boolean>
}
