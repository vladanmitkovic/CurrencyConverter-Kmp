package me.mitkovic.kmp.currencyconverter.data.local.conversionrates

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import me.mitkovic.kmp.currencyconverter.data.local.database.ConversionRatesEntity
import me.mitkovic.kmp.currencyconverter.data.local.database.CurrencyConverterDatabase
import me.mitkovic.kmp.currencyconverter.data.mapper.toDomain
import me.mitkovic.kmp.currencyconverter.data.mapper.toEntityJsonString
import me.mitkovic.kmp.currencyconverter.domain.model.ConversionRatesResponse

open class ConversionRatesDataSourceImpl(
    private val database: CurrencyConverterDatabase,
    private val json: Json,
) : IConversionRatesDataSource {

    override suspend fun saveConversionRates(response: ConversionRatesResponse) {
        val conversionRatesJson = response.toEntityJsonString(json)
        database.conversionRatesDao().upsert(
            ConversionRatesEntity(
                id = 1L,
                timestamp = response.timestamp,
                rates = conversionRatesJson,
            ),
        )
    }

    override fun getConversionRates(): Flow<ConversionRatesResponse?> =
        database.conversionRatesDao()
            .observe()
            .map { entity ->
                entity?.toDomain(json)
            }
}
