package me.mitkovic.kmp.currencyconverter.data.local

import me.mitkovic.kmp.currencyconverter.data.local.conversionrates.IConversionRatesDataSource
import me.mitkovic.kmp.currencyconverter.data.local.favorites.IFavoritesDataSource
import me.mitkovic.kmp.currencyconverter.data.local.selectedcurrencies.ISelectedCurrenciesDataSource
import me.mitkovic.kmp.currencyconverter.data.local.theme.IThemeDataSource

class LocalDataSourceImpl(
    override val conversionRates: IConversionRatesDataSource,
    override val theme: IThemeDataSource,
    override val favorites: IFavoritesDataSource,
    override val selectedCurrencies: ISelectedCurrenciesDataSource,
) : ILocalDataSource
