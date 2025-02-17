package me.mitkovic.kmp.currencyconverter.di

import me.mitkovic.kmp.currencyconverter.data.local.LocalDataSource
import me.mitkovic.kmp.currencyconverter.data.remote.RemoteDataSource
import me.mitkovic.kmp.currencyconverter.data.repository.ConversionRatesRepository
import me.mitkovic.kmp.currencyconverter.data.repository.ConversionRatesRepositoryImpl
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
    }

expect fun platformModule(): Module

fun initKoin(appDeclaration: KoinApplication.() -> Unit = {}) {
    startKoin {
        modules(commonModule, platformModule(), viewModelModule)
        appDeclaration()
    }
}
