package me.mitkovic.kmp.currencyconverter.data.local.conversionrates

import kotlinx.coroutines.flow.Flow
import me.mitkovic.kmp.currencyconverter.data.model.ConversionRatesResponse

interface ConversionRatesDataSource {

    suspend fun saveConversionRates(response: ConversionRatesResponse)

    fun getConversionRates(): Flow<ConversionRatesResponse?>
}
