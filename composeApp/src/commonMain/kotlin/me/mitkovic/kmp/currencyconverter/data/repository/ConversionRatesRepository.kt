package me.mitkovic.kmp.currencyconverter.data.repository

import kotlinx.coroutines.flow.Flow
import me.mitkovic.kmp.currencyconverter.data.model.ConversionRatesResponse
import me.mitkovic.kmp.currencyconverter.data.model.NetworkResult

interface ConversionRatesRepository {

    fun getConversionRates(): Flow<NetworkResult<ConversionRatesResponse?>>

    fun refreshConversionRates(): Flow<NetworkResult<ConversionRatesResponse?>>
}
