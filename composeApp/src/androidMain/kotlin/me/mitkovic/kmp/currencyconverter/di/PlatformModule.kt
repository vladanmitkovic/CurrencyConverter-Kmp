package me.mitkovic.kmp.currencyconverter.di

import me.mitkovic.kmp.currencyconverter.logging.AndroidLogger
import me.mitkovic.kmp.currencyconverter.logging.AppLogger
import org.koin.dsl.module

actual fun platformModule() =
    module {
        single<AppLogger> { AndroidLogger() }
    }
