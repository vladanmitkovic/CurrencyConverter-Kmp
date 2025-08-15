package me.mitkovic.kmp.currencyconverter.data.repository.conversionrates

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import me.mitkovic.kmp.currencyconverter.data.local.LocalDataSource
import me.mitkovic.kmp.currencyconverter.data.model.Resource
import me.mitkovic.kmp.currencyconverter.data.model.toDomainModel
import me.mitkovic.kmp.currencyconverter.data.remote.RemoteDataSource
import me.mitkovic.kmp.currencyconverter.domain.model.ConversionRatesResponse
import me.mitkovic.kmp.currencyconverter.domain.repository.ConversionRatesRepository
import me.mitkovic.kmp.currencyconverter.logging.AppLogger

class ConversionRatesRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val logger: AppLogger,
) : ConversionRatesRepository {

    // This method emits locally stored conversion rates.
    override fun getConversionRates(): Flow<Resource<ConversionRatesResponse?>> =
        localDataSource.conversionRates
            .getConversionRates()
            .map { localResponse ->
                val result: Resource<ConversionRatesResponse?> = Resource.Success(localResponse?.toDomainModel())
                result
            }.catch { e ->
                logger.logError(
                    ConversionRatesRepositoryImpl::class.simpleName,
                    "Error fetching local conversion rates: ${e.message}",
                    e,
                )
                emit(Resource.Error(e.message ?: "Unknown error", e))
            }

    // This method refreshes the conversion rates from the remote source.
    override suspend fun refreshConversionRates(): Resource<ConversionRatesResponse?> {
        try {
            val remoteResult = remoteDataSource.getConversionRates()
            return when (remoteResult) {
                is Resource.Success -> {
                    // Save the successful remote data into the local database
                    localDataSource.conversionRates.saveConversionRates(remoteResult.data)
                    logger.logDebug(
                        ConversionRatesRepositoryImpl::class.simpleName,
                        "Database updated with remote data (timestamp: ${remoteResult.data.timestamp})",
                    )
                    Resource.Success(remoteResult.data.toDomainModel())
                }
                is Resource.Error -> {
                    logger.logError(
                        ConversionRatesRepositoryImpl::class.simpleName,
                        "Remote error: ${remoteResult.message}",
                        remoteResult.throwable,
                    )
                    remoteResult
                }
                else -> {
                    // Remote shouldn't return Loading, but handle gracefully if it does
                    Resource.Error("Unexpected state from remote")
                }
            }
        } catch (e: Exception) {
            logger.logError(
                ConversionRatesRepositoryImpl::class.simpleName,
                "Error refreshing conversion rates: ${e.message}",
                e,
            )
            return Resource.Error(e.message ?: "Unknown error", throwable = e)
        }
    }
}
