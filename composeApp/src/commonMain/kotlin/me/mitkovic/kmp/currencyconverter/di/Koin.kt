package me.mitkovic.kmp.currencyconverter.di

import me.mitkovic.kmp.currencyconverter.data.local.LocalDataSource
import me.mitkovic.kmp.currencyconverter.data.remote.RemoteDataSource
import me.mitkovic.kmp.currencyconverter.data.repository.CurrencyConverterRepository
import me.mitkovic.kmp.currencyconverter.data.repository.CurrencyConverterRepositoryImpl
import me.mitkovic.kmp.currencyconverter.data.repository.conversionrates.ConversionRatesRepositoryImpl
import me.mitkovic.kmp.currencyconverter.data.repository.favorites.FavoritesRepository
import me.mitkovic.kmp.currencyconverter.data.repository.favorites.FavoritesRepositoryImpl
import me.mitkovic.kmp.currencyconverter.data.repository.selectedcurrencies.SelectedCurrenciesRepository
import me.mitkovic.kmp.currencyconverter.data.repository.selectedcurrencies.SelectedCurrenciesRepositoryImpl
import me.mitkovic.kmp.currencyconverter.data.repository.theme.ThemeRepository
import me.mitkovic.kmp.currencyconverter.data.repository.theme.ThemeRepositoryImpl
import me.mitkovic.kmp.currencyconverter.domain.repository.ConversionRatesRepository
import me.mitkovic.kmp.currencyconverter.logging.AppLogger
import me.mitkovic.kmp.currencyconverter.platform.Platform
import me.mitkovic.kmp.currencyconverter.platform.getPlatform
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

val commonModule =
    module {

        single<Platform> { getPlatform() }

        single<ConversionRatesRepository> {
            ConversionRatesRepositoryImpl(
                localDataSource = get<LocalDataSource>(),
                remoteDataSource = get<RemoteDataSource>(),
                logger = get<AppLogger>(),
            )
        }

        single<ThemeRepository> {
            ThemeRepositoryImpl(
                localDataSource = get<LocalDataSource>(),
            )
        }

        single<FavoritesRepository> {
            FavoritesRepositoryImpl(
                localDataSource = get<LocalDataSource>(),
            )
        }

        single<SelectedCurrenciesRepository> {
            SelectedCurrenciesRepositoryImpl(
                localDataSource = get<LocalDataSource>(),
            )
        }

        single<CurrencyConverterRepository> {
            CurrencyConverterRepositoryImpl(
                conversionRatesRepository = get<ConversionRatesRepository>(),
                themeRepository = get<ThemeRepository>(),
                favoritesRepository = get<FavoritesRepository>(),
                selectedCurrenciesRepository = get<SelectedCurrenciesRepository>(),
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
