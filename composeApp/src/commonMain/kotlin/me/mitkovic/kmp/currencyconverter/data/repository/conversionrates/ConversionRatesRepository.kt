package me.mitkovic.kmp.currencyconverter.data.repository.conversionrates

import kotlinx.coroutines.flow.Flow
import me.mitkovic.kmp.currencyconverter.data.model.ConversionRatesResponse
import me.mitkovic.kmp.currencyconverter.data.model.Resource

interface ConversionRatesRepository {

    fun getConversionRates(): Flow<Resource<ConversionRatesResponse?>>

    fun refreshConversionRates(): Flow<Resource<ConversionRatesResponse?>>
}
