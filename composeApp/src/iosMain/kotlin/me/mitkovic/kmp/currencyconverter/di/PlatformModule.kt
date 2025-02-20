package me.mitkovic.kmp.currencyconverter.di

import app.cash.sqldelight.driver.native.NativeSqliteDriver
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import me.mitkovic.kmp.currencyconverter.common.ConnectivityObserver
import me.mitkovic.kmp.currencyconverter.common.ConnectivityObserverImpl
import me.mitkovic.kmp.currencyconverter.data.local.LocalDataSource
import me.mitkovic.kmp.currencyconverter.data.local.LocalDataSourceImpl
import me.mitkovic.kmp.currencyconverter.data.local.database.CurrencyConverterDatabase
import me.mitkovic.kmp.currencyconverter.data.local.theme.ThemeDataSource
import me.mitkovic.kmp.currencyconverter.data.local.theme.ThemeDataSourceImpl
import me.mitkovic.kmp.currencyconverter.data.remote.RemoteDataSource
import me.mitkovic.kmp.currencyconverter.data.remote.RemoteDataSourceImpl
import me.mitkovic.kmp.currencyconverter.logging.AppLogger
import me.mitkovic.kmp.currencyconverter.logging.AppLoggerImpl
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

actual fun platformModule() =
    module {
        single<AppLogger> {
            AppLoggerImpl()
        }

        single<NSUserDefaults> {
            NSUserDefaults.standardUserDefaults()
        }

        single {
            NativeSqliteDriver(
                schema = CurrencyConverterDatabase.Schema,
                name = "currency_converter.db",
            )
        }

        single {
            CurrencyConverterDatabase(
                driver = get<NativeSqliteDriver>(),
            )
        }

        single {
            Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            }
        }

        single<ThemeDataSource> {
            ThemeDataSourceImpl(
                defaults = get<NSUserDefaults>(),
            )
        }
        single<LocalDataSource> {
            LocalDataSourceImpl(
                database = get<CurrencyConverterDatabase>(),
                json = get<Json>(),
                theme = get<ThemeDataSource>(),
            )
        }

        single<ConnectivityObserver> {
            ConnectivityObserverImpl()
        }

        single {
            HttpClient(Darwin) {
                install(ContentNegotiation) {
                    json(
                        Json {
                            ignoreUnknownKeys = true
                            prettyPrint = true
                            isLenient = true
                        },
                    )
                }
            }
        }

        single<RemoteDataSource> {
            RemoteDataSourceImpl(
                client = get<HttpClient>(),
                logger = get<AppLogger>(),
            )
        }
    }
