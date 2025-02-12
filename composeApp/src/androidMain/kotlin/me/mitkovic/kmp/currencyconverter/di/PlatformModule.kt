package me.mitkovic.kmp.currencyconverter.di

import me.mitkovic.kmp.currencyconverter.data.local.LocalDataSource
import me.mitkovic.kmp.currencyconverter.data.local.LocalDataSourceImpl
import me.mitkovic.kmp.currencyconverter.logging.AndroidLogger
import me.mitkovic.kmp.currencyconverter.logging.AppLogger
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual fun platformModule() =
    module {
        single<AppLogger> { AndroidLogger() }

        single<LocalDataSource> {
            LocalDataSourceImpl(
                context = androidContext(),
            )
        }
    }
