package me.mitkovic.kmp.currencyconverter.data.remote

import me.mitkovic.kmp.currencyconverter.data.model.network.ConversionRatesResponseDto
import me.mitkovic.kmp.currencyconverter.domain.model.Resource

interface IRemoteDataSource {

    suspend fun getConversionRates(): Resource<ConversionRatesResponseDto>
}
