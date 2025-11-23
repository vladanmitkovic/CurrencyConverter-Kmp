package me.mitkovic.kmp.currencyconverter.domain.repository

import kotlinx.coroutines.flow.Flow
import me.mitkovic.kmp.currencyconverter.domain.model.ConversionRatesResponse
import me.mitkovic.kmp.currencyconverter.domain.model.Resource

interface IConversionRatesRepository {

    fun getConversionRates(): Flow<Resource<ConversionRatesResponse?>>

    suspend fun refreshConversionRates(): Resource<ConversionRatesResponse?>
}
