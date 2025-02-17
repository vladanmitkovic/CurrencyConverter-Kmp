package me.mitkovic.kmp.currencyconverter.data.local

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

abstract class SharedLocalDataSource(
    private val database: CurrencyConverterDatabase,
    private val json: Json,
) : LocalDataSource {

    // Conversion rates implementations (common to all platforms)
    override suspend fun saveConversionRates(response: ConversionRatesResponse) {
        val conversionRatesJson =
            json.encodeToString(
                MapSerializer(String.serializer(), Double.serializer()),
                response.conversion_rates,
            )
        database.currencyConverterDatabaseQueries.insertConversionRates(
            result = response.result,
            timestamp = response.timestamp,
            base_currency = response.base_currency,
            conversion_rates = conversionRatesJson,
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
                        result = row.result,
                        timestamp = row.timestamp,
                        base_currency = row.base_currency,
                        conversion_rates =
                            json.decodeFromString(
                                MapSerializer(String.serializer(), Double.serializer()),
                                row.conversion_rates,
                            ),
                    )
                }
            }

    // Theme persistence methods remain abstract so each platform can implement them as needed.
    abstract override suspend fun saveTheme(isDarkMode: Boolean)

    abstract override fun getTheme(): Flow<Boolean>
}
