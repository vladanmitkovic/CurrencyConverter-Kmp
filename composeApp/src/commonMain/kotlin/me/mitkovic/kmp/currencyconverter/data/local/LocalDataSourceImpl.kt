package me.mitkovic.kmp.currencyconverter.data.local

import kotlinx.serialization.json.Json
import me.mitkovic.kmp.currencyconverter.data.local.conversionrates.ConversionRatesDataSourceImpl
import me.mitkovic.kmp.currencyconverter.data.local.conversionrates.IConversionRatesDataSource
import me.mitkovic.kmp.currencyconverter.data.local.database.CurrencyConverterDatabase
import me.mitkovic.kmp.currencyconverter.data.local.favorites.IFavoritesDataSource
import me.mitkovic.kmp.currencyconverter.data.local.selectedcurrencies.ISelectedCurrenciesDataSource
import me.mitkovic.kmp.currencyconverter.data.local.theme.IThemeDataSource

class LocalDataSourceImpl(
    database: CurrencyConverterDatabase,
    json: Json,
    override val theme: IThemeDataSource,
    override val favorites: IFavoritesDataSource,
    override val selectedCurrencies: ISelectedCurrenciesDataSource,
) : ConversionRatesDataSourceImpl(database, json),
    ILocalDataSource {

    override val conversionRates: IConversionRatesDataSource get() = this
}
