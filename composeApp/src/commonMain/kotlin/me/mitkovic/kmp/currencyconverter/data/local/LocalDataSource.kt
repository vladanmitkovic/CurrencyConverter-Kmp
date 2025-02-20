package me.mitkovic.kmp.currencyconverter.data.local

import me.mitkovic.kmp.currencyconverter.data.local.conversionrates.ConversionRatesDataSource
import me.mitkovic.kmp.currencyconverter.data.local.favorites.FavoritesDataSource
import me.mitkovic.kmp.currencyconverter.data.local.theme.ThemeDataSource

interface LocalDataSource {
    val conversionRates: ConversionRatesDataSource
    val theme: ThemeDataSource
    val favorites: FavoritesDataSource
}
