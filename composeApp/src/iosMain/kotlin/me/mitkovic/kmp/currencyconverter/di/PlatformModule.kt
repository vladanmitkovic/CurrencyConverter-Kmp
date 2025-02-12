package me.mitkovic.kmp.currencyconverter.di

import me.mitkovic.kmp.currencyconverter.data.local.LocalDataSource
import me.mitkovic.kmp.currencyconverter.data.local.LocalDataSourceImpl
import me.mitkovic.kmp.currencyconverter.logging.AppLogger
import me.mitkovic.kmp.currencyconverter.logging.IosLogger
import org.koin.dsl.module

actual fun platformModule() =
    module {
        single<AppLogger> {
            IosLogger()
        }

        single<LocalDataSource> {
            LocalDataSourceImpl()
        }
    }
