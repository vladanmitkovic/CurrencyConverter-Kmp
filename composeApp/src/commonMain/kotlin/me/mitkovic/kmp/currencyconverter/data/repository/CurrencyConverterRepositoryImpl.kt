package me.mitkovic.kmp.currencyconverter.data.repository

import me.mitkovic.kmp.currencyconverter.data.repository.conversionrates.ConversionRatesRepository
import me.mitkovic.kmp.currencyconverter.data.repository.favorites.FavoritesRepository
import me.mitkovic.kmp.currencyconverter.data.repository.selectedcurrencies.SelectedCurrenciesRepository
import me.mitkovic.kmp.currencyconverter.data.repository.theme.ThemeRepository

class CurrencyConverterRepositoryImpl(
    override val conversionRatesRepository: ConversionRatesRepository,
    override val themeRepository: ThemeRepository,
    override val favoritesRepository: FavoritesRepository,
    override val selectedCurrenciesRepository: SelectedCurrenciesRepository,
) : CurrencyConverterRepository
