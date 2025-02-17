package me.mitkovic.kmp.currencyconverter.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.mitkovic.kmp.currencyconverter.data.local.LocalDataSource
import me.mitkovic.kmp.currencyconverter.data.model.ConversionRatesResponse
import me.mitkovic.kmp.currencyconverter.data.model.NetworkResult
import me.mitkovic.kmp.currencyconverter.data.remote.RemoteDataSource
import me.mitkovic.kmp.currencyconverter.logging.AppLogger

class ConversionRatesRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val logger: AppLogger,
) : ConversionRatesRepository {

    // This method emits locally stored conversion rates.
    override fun getConversionRates(): Flow<NetworkResult<ConversionRatesResponse?>> =
        flow {
            try {
                // Collect the local data source flow and emit its values.
                localDataSource.getConversionRates().collect { localResponse ->
                    emit(NetworkResult.Success(localResponse))
                }
            } catch (e: Exception) {
                logger.logError(
                    ConversionRatesRepositoryImpl::class.simpleName,
                    "Error fetching local conversion rates: ${e.message}",
                    e,
                )
            }
        }

    // This method refreshes the conversion rates from the remote source.
    override fun refreshConversionRates(): Flow<NetworkResult<ConversionRatesResponse?>> =
        flow {
            try {
                remoteDataSource.getConversionRates().collect { remoteResult ->
                    when (remoteResult) {
                        is NetworkResult.Success -> {
                            // Save the successful remote data into the local database.
                            localDataSource.saveConversionRates(remoteResult.data)
                            emit(NetworkResult.Success(remoteResult.data))
                        }
                        is NetworkResult.Error -> {
                            logger.logError(
                                ConversionRatesRepositoryImpl::class.simpleName,
                                "Remote error: ${remoteResult.throwable}",
                                remoteResult.throwable,
                            )
                            emit(remoteResult)
                        }
                        is NetworkResult.Loading -> {
                            emit(remoteResult)
                        }
                    }
                }
            } catch (e: Exception) {
                logger.logError(
                    ConversionRatesRepositoryImpl::class.simpleName,
                    "Error refreshing conversion rates: ${e.message}",
                    e,
                )
            }
        }
}
