package me.mitkovic.kmp.currencyconverter.data.local.theme

import kotlinx.coroutines.flow.Flow

interface IThemeDataSource {

    suspend fun saveTheme(isLightMode: Boolean)

    fun getTheme(): Flow<Boolean>
}
