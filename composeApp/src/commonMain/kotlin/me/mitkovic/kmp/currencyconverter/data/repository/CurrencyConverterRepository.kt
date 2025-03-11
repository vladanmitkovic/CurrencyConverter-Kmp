package me.mitkovic.kmp.currencyconverter.data.repository

import me.mitkovic.kmp.currencyconverter.data.repository.favorites.FavoritesRepository
import me.mitkovic.kmp.currencyconverter.data.repository.selectedcurrencies.SelectedCurrenciesRepository
import me.mitkovic.kmp.currencyconverter.data.repository.theme.ThemeRepository
import me.mitkovic.kmp.currencyconverter.domain.repository.ConversionRatesRepository

interface CurrencyConverterRepository {
    val conversionRatesRepository: ConversionRatesRepository
    val themeRepository: ThemeRepository
    val favoritesRepository: FavoritesRepository
    val selectedCurrenciesRepository: SelectedCurrenciesRepository
}
