package me.mitkovic.kmp.currencyconverter.data.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.mitkovic.kmp.currencyconverter.data.model.ConversionRatesResponse
import me.mitkovic.kmp.currencyconverter.data.model.NetworkResult
import me.mitkovic.kmp.currencyconverter.logging.AppLogger

class RemoteDataSourceImpl(
    private val apiService: ApiService,
    private val logger: AppLogger,
) : RemoteDataSource {

    override suspend fun getConversionRates(): Flow<NetworkResult<ConversionRatesResponse>> =
        flow {
            emit(NetworkResult.Loading)
            try {
                val conversionRates =
                    apiService.getConversionRates(
                        "apiKey",
                    )
                emit(NetworkResult.Success(conversionRates))
            } catch (e: Exception) {
                logger.logError("RemoteDataSourceImpl getConversionRates error: $e", null)
                // Don't emit error, throw to let repository handle fallback
                throw e
            }
        }
}
