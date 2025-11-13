package me.mitkovic.kmp.currencyconverter.di

import me.mitkovic.kmp.currencyconverter.data.repository.ICurrencyConverterRepository
import me.mitkovic.kmp.currencyconverter.logging.IAppLogger
import me.mitkovic.kmp.currencyconverter.ui.AppViewModel
import me.mitkovic.kmp.currencyconverter.ui.screens.converter.ConverterViewModel
import me.mitkovic.kmp.currencyconverter.ui.screens.favorites.FavoritesViewModel
import org.koin.dsl.module

val viewModelModule =
    module {
        factory {
            AppViewModel(
                currencyConverterRepository = get<ICurrencyConverterRepository>(),
                logger = get<IAppLogger>(),
            )
        }
        factory {
            ConverterViewModel(
                currencyConverterRepository = get<ICurrencyConverterRepository>(),
                logger = get<IAppLogger>(),
            )
        }
        factory {
            FavoritesViewModel(
                currencyConverterRepository = get<ICurrencyConverterRepository>(),
                logger = get<IAppLogger>(),
            )
        }
    }
