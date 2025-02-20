package me.mitkovic.kmp.currencyconverter.data.local

import kotlinx.serialization.json.Json
import me.mitkovic.kmp.currencyconverter.data.local.conversionrates.ConversionRatesDataSource
import me.mitkovic.kmp.currencyconverter.data.local.conversionrates.ConversionRatesDataSourceImpl
import me.mitkovic.kmp.currencyconverter.data.local.database.CurrencyConverterDatabase
import me.mitkovic.kmp.currencyconverter.data.local.favorites.FavoritesDataSource
import me.mitkovic.kmp.currencyconverter.data.local.selectedcurrencies.SelectedCurrenciesDataSource
import me.mitkovic.kmp.currencyconverter.data.local.theme.ThemeDataSource

class LocalDataSourceImpl(
    database: CurrencyConverterDatabase,
    json: Json,
    override val theme: ThemeDataSource,
    override val favorites: FavoritesDataSource,
    override val selectedCurrencies: SelectedCurrenciesDataSource,
) : ConversionRatesDataSourceImpl(database, json),
    LocalDataSource {

    override val conversionRates: ConversionRatesDataSource get() = this
}
