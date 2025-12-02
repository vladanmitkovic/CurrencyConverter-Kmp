package me.mitkovic.kmp.currencyconverter.di

import me.mitkovic.kmp.currencyconverter.data.repository.ICurrencyConverterRepository
import me.mitkovic.kmp.currencyconverter.logging.IAppLogger
import me.mitkovic.kmp.currencyconverter.ui.AppViewModel
import me.mitkovic.kmp.currencyconverter.ui.screens.converter.ConverterViewModel
import me.mitkovic.kmp.currencyconverter.ui.screens.favorites.FavoritesViewModel
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class ViewModelModule {

    @Factory
    fun provideAppViewModel(
        currencyConverterRepository: ICurrencyConverterRepository,
        logger: IAppLogger,
    ): AppViewModel =
        AppViewModel(
            currencyConverterRepository = currencyConverterRepository,
            logger = logger,
        )

    @Factory
    fun provideConverterViewModel(
        currencyConverterRepository: ICurrencyConverterRepository,
        logger: IAppLogger,
    ): ConverterViewModel =
        ConverterViewModel(
            currencyConverterRepository = currencyConverterRepository,
            logger = logger,
        )

    @Factory
    fun provideFavoritesViewModel(
        currencyConverterRepository: ICurrencyConverterRepository,
        logger: IAppLogger,
    ): FavoritesViewModel =
        FavoritesViewModel(
            currencyConverterRepository = currencyConverterRepository,
            logger = logger,
        )
}
