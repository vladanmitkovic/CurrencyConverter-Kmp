package me.mitkovic.kmp.currencyconverter.data.repository

import me.mitkovic.kmp.currencyconverter.data.repository.conversionrates.ConversionRatesRepository
import me.mitkovic.kmp.currencyconverter.data.repository.theme.ThemeRepository

class CurrencyConverterRepositoryImpl(
    override val conversionRatesRepository: ConversionRatesRepository,
    override val themeRepository: ThemeRepository,
) : CurrencyConverterRepository
