package me.mitkovic.kmp.currencyconverter.di

import me.mitkovic.kmp.currencyconverter.data.repository.CurrencyConverterRepository
import me.mitkovic.kmp.currencyconverter.logging.AppLogger
import me.mitkovic.kmp.currencyconverter.navigation.NavigationViewModel
import me.mitkovic.kmp.currencyconverter.ui.AppViewModel
import me.mitkovic.kmp.currencyconverter.ui.screens.converter.ConverterViewModel
import me.mitkovic.kmp.currencyconverter.ui.screens.favorites.FavoritesViewModel
import org.koin.dsl.module

val viewModelModule =
    module {
        factory {
            NavigationViewModel()
        }
        factory {
            AppViewModel(
                currencyConverterRepository = get<CurrencyConverterRepository>(),
                logger = get<AppLogger>(),
            )
        }
        factory {
            ConverterViewModel(
                currencyConverterRepository = get<CurrencyConverterRepository>(),
                logger = get<AppLogger>(),
            )
        }
        factory {
            FavoritesViewModel(
                currencyConverterRepository = get<CurrencyConverterRepository>(),
                logger = get<AppLogger>(),
            )
        }
    }
