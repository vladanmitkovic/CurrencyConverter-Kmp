package me.mitkovic.kmp.currencyconverter.data.local

import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun saveTheme(isDarkMode: Boolean)

    fun getTheme(): Flow<Boolean>
}
