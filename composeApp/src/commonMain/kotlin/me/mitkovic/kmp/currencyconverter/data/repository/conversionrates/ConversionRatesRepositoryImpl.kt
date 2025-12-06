package me.mitkovic.kmp.currencyconverter.data.repository.conversionrates

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import me.mitkovic.kmp.currencyconverter.data.local.ILocalDataSource
import me.mitkovic.kmp.currencyconverter.data.mapper.toDomain
import me.mitkovic.kmp.currencyconverter.data.model.Resource
import me.mitkovic.kmp.currencyconverter.data.remote.IRemoteDataSource
import me.mitkovic.kmp.currencyconverter.domain.model.ConversionRatesResponse
import me.mitkovic.kmp.currencyconverter.domain.repository.IConversionRatesRepository
import me.mitkovic.kmp.currencyconverter.logging.IAppLogger

class ConversionRatesRepositoryImpl(
    private val localDataSource: ILocalDataSource,
    private val remoteDataSource: IRemoteDataSource,
    private val logger: IAppLogger,
) : IConversionRatesRepository {

    override fun getConversionRates(): Flow<Resource<ConversionRatesResponse?>> =
        localDataSource.conversionRates
            .getConversionRates()
            .map { domainModel ->
                val result: Resource<ConversionRatesResponse?> = Resource.Success(domainModel)
                result
            }.catch { e ->
                logger.logError(
                    ConversionRatesRepositoryImpl::class.simpleName,
                    "Error fetching local conversion rates: ${e.message}",
                    e,
                )
                emit(Resource.Error(e.message ?: "Unknown error", exception = e))
            }

    override suspend fun refreshConversionRates(): Resource<ConversionRatesResponse?> =
        try {
            when (val remoteResult = remoteDataSource.getConversionRates()) {
                is Resource.Success -> {
                    val responseDto = remoteResult.data
                    if (responseDto != null) {
                        val domainModel = responseDto.toDomain()
                        localDataSource.conversionRates.saveConversionRates(domainModel)
                        logger.logDebug(
                            ConversionRatesRepositoryImpl::class.simpleName,
                            "Database updated with remote data (timestamp: ${domainModel.timestamp})",
                        )
                        Resource.Success(domainModel)
                    } else {
                        Resource.Error("Empty response from server")
                    }
                }

                is Resource.Error -> {
                    logger.logError(
                        ConversionRatesRepositoryImpl::class.simpleName,
                        "Remote error: ${remoteResult.message}",
                        remoteResult.exception,
                    )
                    Resource.Error(
                        message = remoteResult.message ?: "Unknown error",
                        exception = remoteResult.exception,
                    )
                }

                is Resource.Loading -> {
                    Resource.Error("Unexpected state from remote")
                }
            }
        } catch (e: Exception) {
            logger.logError(
                ConversionRatesRepositoryImpl::class.simpleName,
                "Error refreshing conversion rates: ${e.message}",
                e,
            )
            Resource.Error(e.message ?: "Unknown error", exception = e)
        }
}
