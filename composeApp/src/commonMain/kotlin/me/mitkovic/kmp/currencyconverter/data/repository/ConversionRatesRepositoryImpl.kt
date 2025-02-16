package me.mitkovic.kmp.currencyconverter.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.mitkovic.kmp.currencyconverter.data.model.ConversionRatesResponse
import me.mitkovic.kmp.currencyconverter.data.model.NetworkResult
import me.mitkovic.kmp.currencyconverter.data.remote.RemoteDataSource
import me.mitkovic.kmp.currencyconverter.logging.AppLogger

class ConversionRatesRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val logger: AppLogger,
) : ConversionRatesRepository {

    override fun getConversionRates(): Flow<NetworkResult<ConversionRatesResponse?>> =
        flow {
            try {
                // Try fetching from remote
                remoteDataSource.getConversionRates().collect { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            emit(NetworkResult.Success(result.data))
                        }
                        else -> {
                            logger.logError("result: $result", null, ConversionRatesRepositoryImpl::class.simpleName)
                        } // Ignore other states
                    }
                }
            } catch (e: Exception) {
                logger.logError("Error: $e", e, ConversionRatesRepositoryImpl::class.simpleName)
            }
        }
}
