package me.mitkovic.kmp.currencyconverter.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.json.Json
import me.mitkovic.kmp.currencyconverter.data.local.database.CurrencyConverterDatabase

class LocalDataSourceImpl(
    database: CurrencyConverterDatabase,
    json: Json,
) : SharedLocalDataSource(database, json) {

    private val isDarkModeFlow = MutableStateFlow(false)

    override suspend fun saveTheme(isDarkMode: Boolean) {
        isDarkModeFlow.value = isDarkMode
    }

    override fun getTheme(): Flow<Boolean> = isDarkModeFlow
}
