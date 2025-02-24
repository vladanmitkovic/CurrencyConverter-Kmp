package me.mitkovic.kmp.currencyconverter.data.local.conversionrates

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import me.mitkovic.kmp.currencyconverter.data.local.database.CurrencyConverterDatabase
import me.mitkovic.kmp.currencyconverter.data.model.ConversionRatesResponse

open class ConversionRatesDataSourceImpl(
    private val database: CurrencyConverterDatabase,
    private val json: Json,
) : ConversionRatesDataSource {

    override suspend fun saveConversionRates(response: ConversionRatesResponse) {
        val conversionRatesJson =
            json.encodeToString(
                MapSerializer(String.serializer(), Double.serializer()),
                response.conversion_rates,
            )
        database.currencyConverterDatabaseQueries.insertConversionRates(
            timestamp = response.timestamp,
            rates = conversionRatesJson,
        )
    }

    override fun getConversionRates(): Flow<ConversionRatesResponse?> =
        database.currencyConverterDatabaseQueries
            .getConversionRates()
            .asFlow()
            .mapToList(context = Dispatchers.Default)
            .map { list ->
                list.firstOrNull()?.let { row ->
                    ConversionRatesResponse(
                        timestamp = row.timestamp,
                        conversion_rates =
                            json.decodeFromString(
                                MapSerializer(String.serializer(), Double.serializer()),
                                row.rates,
                            ),
                    )
                }
            }
}
