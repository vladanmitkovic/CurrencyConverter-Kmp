package me.mitkovic.kmp.currencyconverter.data.repository

import me.mitkovic.kmp.currencyconverter.data.repository.conversionrates.ConversionRatesRepository
import me.mitkovic.kmp.currencyconverter.data.repository.favorites.FavoritesRepository
import me.mitkovic.kmp.currencyconverter.data.repository.theme.ThemeRepository

interface CurrencyConverterRepository {
    val conversionRatesRepository: ConversionRatesRepository
    val themeRepository: ThemeRepository
    val favoritesRepository: FavoritesRepository
}
