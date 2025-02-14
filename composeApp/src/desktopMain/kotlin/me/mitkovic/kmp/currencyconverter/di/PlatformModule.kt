package me.mitkovic.kmp.currencyconverter.di

import me.mitkovic.kmp.currencyconverter.common.ConnectivityObserver
import me.mitkovic.kmp.currencyconverter.common.ConnectivityObserverImpl
import me.mitkovic.kmp.currencyconverter.data.local.LocalDataSource
import me.mitkovic.kmp.currencyconverter.data.local.LocalDataSourceImpl
import me.mitkovic.kmp.currencyconverter.logging.AppLogger
import me.mitkovic.kmp.currencyconverter.logging.AppLoggerImpl
import org.koin.dsl.module

actual fun platformModule() =
    module {
        single<AppLogger> {
            AppLoggerImpl()
        }

        single<LocalDataSource> {
            LocalDataSourceImpl()
        }

        single<ConnectivityObserver> {
            ConnectivityObserverImpl()
        }
    }
