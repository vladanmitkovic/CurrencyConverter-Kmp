package me.mitkovic.kmp.currencyconverter.domain.repository

import kotlinx.coroutines.flow.Flow
import me.mitkovic.kmp.currencyconverter.data.model.Resource
import me.mitkovic.kmp.currencyconverter.domain.model.ConversionRatesResponse

interface ConversionRatesRepository {

    fun getConversionRates(): Flow<Resource<ConversionRatesResponse?>>

    suspend fun refreshConversionRates(): Resource<ConversionRatesResponse?>
}
