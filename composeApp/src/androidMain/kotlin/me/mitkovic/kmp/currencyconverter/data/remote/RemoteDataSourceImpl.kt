package me.mitkovic.kmp.currencyconverter.data.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.mitkovic.kmp.currencyconverter.data.model.ConversionRatesResponse
import me.mitkovic.kmp.currencyconverter.data.model.Resource
import me.mitkovic.kmp.currencyconverter.logging.AppLogger

class RemoteDataSourceImpl(
    private val apiService: ApiService,
    private val logger: AppLogger,
) : RemoteDataSource {

    override suspend fun getConversionRates(): Flow<Resource<ConversionRatesResponse>> =
        flow {
            emit(Resource.Loading)
            try {
                val conversionRates =
                    apiService.getConversionRates(
                        "apiKey",
                    )
                emit(Resource.Success(conversionRates))
            } catch (e: Exception) {
                logger.logError(
                    RemoteDataSourceImpl::class.simpleName,
                    "RemoteDataSourceImpl getConversionRates error: ${e.message}",
                    e,
                )
                throw e
            }
        }
}
