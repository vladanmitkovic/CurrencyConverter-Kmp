package me.mitkovic.kmp.currencyconverter.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.flow
import me.mitkovic.kmp.currencyconverter.common.Constants
import me.mitkovic.kmp.currencyconverter.data.model.ConversionRatesResponse
import me.mitkovic.kmp.currencyconverter.data.model.Resource
import me.mitkovic.kmp.currencyconverter.logging.AppLogger

class RemoteDataSourceImpl(
    private val client: HttpClient,
    private val logger: AppLogger,
) : RemoteDataSource {
    override suspend fun getConversionRates() =
        flow {
            emit(Resource.Loading)
            try {
                val resp: ConversionRatesResponse =
                    client
                        .get("${Constants.BASE_URL}/conversion_rates/rates_data.json") {
                            parameter("api_key", "apiKey")
                        }.body()
                emit(Resource.Success(resp))
            } catch (e: Exception) {
                logger.logError("RemoteDataSource", e.message, e)
                // emit(Resource.Error(e.message ?: "Unknown error"))
                throw e
            }
        }
}
