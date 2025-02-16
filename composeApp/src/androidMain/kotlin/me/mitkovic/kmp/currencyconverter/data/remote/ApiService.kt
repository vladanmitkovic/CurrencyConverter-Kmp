package me.mitkovic.kmp.currencyconverter.data.remote

import me.mitkovic.kmp.currencyconverter.data.model.ConversionRatesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/conversion_rates/example_data.json")
    suspend fun getConversionRates(
        @Query("api_key") apiKey: String,
    ): ConversionRatesResponse
}
