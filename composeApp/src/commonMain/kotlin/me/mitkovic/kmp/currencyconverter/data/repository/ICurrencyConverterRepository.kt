package me.mitkovic.kmp.currencyconverter.data.repository

import me.mitkovic.kmp.currencyconverter.data.repository.favorites.IFavoritesRepository
import me.mitkovic.kmp.currencyconverter.data.repository.selectedcurrencies.ISelectedCurrenciesRepository
import me.mitkovic.kmp.currencyconverter.data.repository.theme.IThemeRepository
import me.mitkovic.kmp.currencyconverter.domain.repository.IConversionRatesRepository

interface ICurrencyConverterRepository {
    val conversionRatesRepository: IConversionRatesRepository
    val themeRepository: IThemeRepository
    val favoritesRepository: IFavoritesRepository
    val selectedCurrenciesRepository: ISelectedCurrenciesRepository
}
