package me.mitkovic.kmp.currencyconverter.data.repository.theme

import kotlinx.coroutines.flow.Flow

interface IThemeRepository {

    suspend fun saveTheme(isLightMode: Boolean)

    fun getTheme(): Flow<Boolean>
}
