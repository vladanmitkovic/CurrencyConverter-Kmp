package me.mitkovic.kmp.currencyconverter.di

import me.mitkovic.kmp.currencyconverter.data.local.ILocalDataSource
import me.mitkovic.kmp.currencyconverter.data.remote.IRemoteDataSource
import me.mitkovic.kmp.currencyconverter.data.repository.CurrencyConverterRepositoryImpl
import me.mitkovic.kmp.currencyconverter.data.repository.ICurrencyConverterRepository
import me.mitkovic.kmp.currencyconverter.data.repository.conversionrates.ConversionRatesRepositoryImpl
import me.mitkovic.kmp.currencyconverter.data.repository.favorites.FavoritesRepositoryImpl
import me.mitkovic.kmp.currencyconverter.data.repository.favorites.IFavoritesRepository
import me.mitkovic.kmp.currencyconverter.data.repository.selectedcurrencies.ISelectedCurrenciesRepository
import me.mitkovic.kmp.currencyconverter.data.repository.selectedcurrencies.SelectedCurrenciesRepositoryImpl
import me.mitkovic.kmp.currencyconverter.data.repository.theme.IThemeRepository
import me.mitkovic.kmp.currencyconverter.data.repository.theme.ThemeRepositoryImpl
import me.mitkovic.kmp.currencyconverter.domain.repository.IConversionRatesRepository
import me.mitkovic.kmp.currencyconverter.logging.IAppLogger
import me.mitkovic.kmp.currencyconverter.platform.Platform
import me.mitkovic.kmp.currencyconverter.platform.getPlatform
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

val commonModule =
    module {

        single<Platform> { getPlatform() }

        single<IConversionRatesRepository> {
            ConversionRatesRepositoryImpl(
                localDataSource = get<ILocalDataSource>(),
                remoteDataSource = get<IRemoteDataSource>(),
                logger = get<IAppLogger>(),
            )
        }

        single<IThemeRepository> {
            ThemeRepositoryImpl(
                localDataSource = get<ILocalDataSource>(),
            )
        }

        single<IFavoritesRepository> {
            FavoritesRepositoryImpl(
                localDataSource = get<ILocalDataSource>(),
            )
        }

        single<ISelectedCurrenciesRepository> {
            SelectedCurrenciesRepositoryImpl(
                localDataSource = get<ILocalDataSource>(),
            )
        }

        single<ICurrencyConverterRepository> {
            CurrencyConverterRepositoryImpl(
                conversionRatesRepository = get<IConversionRatesRepository>(),
                themeRepository = get<IThemeRepository>(),
                favoritesRepository = get<IFavoritesRepository>(),
                selectedCurrenciesRepository = get<ISelectedCurrenciesRepository>(),
            )
        }
    }

expect fun platformModule(): Module

fun initKoin(koinContext: KoinApplication.() -> Unit = {}) {
    try {
        startKoin {
            koinContext()
            modules(commonModule, platformModule(), viewModelModule)
        }
    } catch (e: Exception) {
        println("Koin init failed: ${e.message}")
        throw RuntimeException("Koin init failed", e)
    }
}
