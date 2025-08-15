package me.mitkovic.kmp.currencyconverter.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import me.mitkovic.kmp.currencyconverter.common.Constants
import me.mitkovic.kmp.currencyconverter.data.model.ConversionRatesResponse
import me.mitkovic.kmp.currencyconverter.data.model.Resource
import me.mitkovic.kmp.currencyconverter.logging.AppLogger

class RemoteDataSourceImpl(
    private val client: HttpClient,
    private val logger: AppLogger,
) : RemoteDataSource {

    override suspend fun getConversionRates(): Resource<ConversionRatesResponse> {
        try {
            val resp: ConversionRatesResponse =
                client
                    .get("${Constants.BASE_URL}/conversion_rates/rates_data.json") {
                        parameter("api_key", "apiKey")
                    }.body()
            return Resource.Success(resp)
        } catch (e: Exception) {
            logger.logError("RemoteDataSource", e.message, e)
            return Resource.Error(e.message ?: "Unknown error", throwable = e)
        }
    }
}
