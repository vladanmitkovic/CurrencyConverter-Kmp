package me.mitkovic.kmp.currencyconverter.data.local

import me.mitkovic.kmp.currencyconverter.data.local.conversionrates.IConversionRatesDataSource
import me.mitkovic.kmp.currencyconverter.data.local.favorites.IFavoritesDataSource
import me.mitkovic.kmp.currencyconverter.data.local.selectedcurrencies.ISelectedCurrenciesDataSource
import me.mitkovic.kmp.currencyconverter.data.local.theme.IThemeDataSource

interface ILocalDataSource {
    val conversionRates: IConversionRatesDataSource
    val theme: IThemeDataSource
    val favorites: IFavoritesDataSource
    val selectedCurrencies: ISelectedCurrenciesDataSource
}
