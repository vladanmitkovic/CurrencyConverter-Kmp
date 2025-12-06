package me.mitkovic.kmp.currencyconverter.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import me.mitkovic.kmp.currencyconverter.common.Constants
import me.mitkovic.kmp.currencyconverter.data.model.Resource
import me.mitkovic.kmp.currencyconverter.data.model.network.ConversionRatesResponseDto
import me.mitkovic.kmp.currencyconverter.logging.IAppLogger

class RemoteDataSourceImpl(
    private val client: HttpClient,
    private val logger: IAppLogger,
) : IRemoteDataSource {

    override suspend fun getConversionRates(): Resource<ConversionRatesResponseDto> {
        try {
            val resp: ConversionRatesResponseDto =
                client
                    .get("${Constants.BASE_URL}/conversion_rates/rates_data.json") {
                        parameter("api_key", "apiKey")
                    }.body()
            return Resource.Success(resp)
        } catch (e: Exception) {
            logger.logError("RemoteDataSource", e.message, e)
            return Resource.Error(message = e.message ?: "Unknown error", exception = e)
        }
    }
}
