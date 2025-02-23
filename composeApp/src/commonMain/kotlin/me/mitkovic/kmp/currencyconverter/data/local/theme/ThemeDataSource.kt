package me.mitkovic.kmp.currencyconverter.data.local.theme

import kotlinx.coroutines.flow.Flow

interface ThemeDataSource {

    suspend fun saveTheme(isLightMode: Boolean)

    fun getTheme(): Flow<Boolean>
}
