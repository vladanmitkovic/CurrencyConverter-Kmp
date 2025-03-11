package me.mitkovic.kmp.currencyconverter.data.repository.conversionrates

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
        flow {
            try {
                // Collect the local data source flow and emit its values.
                localDataSource.conversionRates.getConversionRates().collect { localResponse ->
                    val domainResponse = localResponse?.toDomainModel()
                    emit(Resource.Success(domainResponse))
                }
            } catch (e: Exception) {
                logger.logError(
                    ConversionRatesRepositoryImpl::class.simpleName,
                    "Error fetching local conversion rates: ${e.message}",
                    e,
                )
                throw e
            }
        }

    // This method refreshes the conversion rates from the remote source.
    override fun refreshConversionRates(): Flow<Resource<ConversionRatesResponse?>> =
        flow {
            try {
                remoteDataSource.getConversionRates().collect { remoteResult ->
                    when (remoteResult) {
                        is Resource.Success -> {
                            // Save the successful remote data into the local database.
                            localDataSource.conversionRates.saveConversionRates(remoteResult.data)
                            val domainResponse = remoteResult.data.toDomainModel()
                            emit(Resource.Success(domainResponse))
                        }
                        is Resource.Error -> {
                            logger.logError(
                                ConversionRatesRepositoryImpl::class.simpleName,
                                "Remote error: ${remoteResult.throwable}",
                                remoteResult.throwable,
                            )
                            emit(remoteResult)
                        }
                        is Resource.Loading -> {
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
                throw e
            }
        }
}
