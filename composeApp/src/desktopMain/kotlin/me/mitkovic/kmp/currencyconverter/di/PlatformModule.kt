package me.mitkovic.kmp.currencyconverter.di

import me.mitkovic.kmp.currencyconverter.logging.AppLogger
import me.mitkovic.kmp.currencyconverter.logging.JvmLogger
import org.koin.dsl.module

actual fun platformModule() =
    module {
        single<AppLogger> { JvmLogger() }
    }
