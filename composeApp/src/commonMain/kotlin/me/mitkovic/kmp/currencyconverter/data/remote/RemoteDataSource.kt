package me.mitkovic.kmp.currencyconverter.data.remote

import kotlinx.coroutines.flow.Flow
import me.mitkovic.kmp.currencyconverter.data.model.ConversionRatesResponse
import me.mitkovic.kmp.currencyconverter.data.model.Resource

interface RemoteDataSource {

    suspend fun getConversionRates(): Flow<Resource<ConversionRatesResponse>>
}
