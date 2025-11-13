package me.mitkovic.kmp.currencyconverter.data.remote

import me.mitkovic.kmp.currencyconverter.data.model.ConversionRatesResponse
import me.mitkovic.kmp.currencyconverter.data.model.Resource

interface IRemoteDataSource {

    suspend fun getConversionRates(): Resource<ConversionRatesResponse>
}
