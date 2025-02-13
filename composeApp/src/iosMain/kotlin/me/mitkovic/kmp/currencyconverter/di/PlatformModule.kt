package me.mitkovic.kmp.currencyconverter.di

import me.mitkovic.kmp.currencyconverter.common.ConnectivityObserver
import me.mitkovic.kmp.currencyconverter.common.NetworkConnectivityObserver
import me.mitkovic.kmp.currencyconverter.data.local.LocalDataSource
import me.mitkovic.kmp.currencyconverter.data.local.LocalDataSourceImpl
import me.mitkovic.kmp.currencyconverter.logging.AppLogger
import me.mitkovic.kmp.currencyconverter.logging.IosLogger
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

actual fun platformModule() =
    module {
        single<AppLogger> {
            IosLogger()
        }

        single<NSUserDefaults> {
            NSUserDefaults.standardUserDefaults()
        }

        single<LocalDataSource> {
            LocalDataSourceImpl(defaults = get<NSUserDefaults>())
        }

        single<ConnectivityObserver> {
            NetworkConnectivityObserver()
        }
    }
