package me.mitkovic.kmp.currencyconverter.data.local

import kotlinx.coroutines.flow.Flow
import me.mitkovic.kmp.currencyconverter.data.model.ConversionRatesResponse

interface LocalDataSource {

    suspend fun saveTheme(isDarkMode: Boolean)

    fun getTheme(): Flow<Boolean>

    suspend fun saveConversionRates(response: ConversionRatesResponse)

    fun getConversionRates(): Flow<ConversionRatesResponse?>
}
