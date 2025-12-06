package me.mitkovic.kmp.currencyconverter.data.remote

import me.mitkovic.kmp.currencyconverter.data.model.Resource
import me.mitkovic.kmp.currencyconverter.data.model.network.ConversionRatesResponseDto

interface IRemoteDataSource {

    suspend fun getConversionRates(): Resource<ConversionRatesResponseDto>
}
