package me.mitkovic.kmp.currencyconverter.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.mitkovic.kmp.currencyconverter.common.Constants
import me.mitkovic.kmp.currencyconverter.data.model.ConversionRatesResponse
import me.mitkovic.kmp.currencyconverter.data.model.Resource
import me.mitkovic.kmp.currencyconverter.logging.AppLogger

class RemoteDataSourceImpl(
    private val client: HttpClient,
    private val logger: AppLogger,
) : RemoteDataSource {

    override suspend fun getConversionRates(): Flow<Resource<ConversionRatesResponse>> =
        flow {
            emit(Resource.Loading)
            try {
                val conversionRatesResponse: ConversionRatesResponse =
                    client
                        .get(
                            "${Constants.BASE_URL}/conversion_rates/example_data.json",
                        ).body()
                emit(Resource.Success(conversionRatesResponse))
            } catch (e: Exception) {
                logger.logError(
                    message = "Failed to get users: ${e.message}",
                    throwable = e,
                    tag = "API",
                )
                // Don't emit error, let repository handle fallback
                throw e
            }
        }
}
