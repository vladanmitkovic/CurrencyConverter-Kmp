package me.mitkovic.kmp.currencyconverter.di

import me.mitkovic.kmp.currencyconverter.data.local.LocalDataSource
import me.mitkovic.kmp.currencyconverter.data.repository.ConversionRatesRepository
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
                localDataSource = get<LocalDataSource>(),
                logger = get<AppLogger>(),
            )
        }
        factory {
            ConverterViewModel(
                repository = get<ConversionRatesRepository>(),
                logger = get<AppLogger>(),
            )
        }
        factory {
            FavoritesViewModel(
                logger = get<AppLogger>(),
            )
        }
    }
