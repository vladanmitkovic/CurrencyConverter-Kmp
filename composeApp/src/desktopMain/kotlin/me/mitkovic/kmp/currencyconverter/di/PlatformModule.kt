package me.mitkovic.kmp.currencyconverter.di

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import io.ktor.client.HttpClient
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

actual fun platformModule() =
    module {
        single<AppLogger> {
            AppLoggerImpl()
        }

        /*
        single {
            JdbcSqliteDriver("jdbc:sqlite:currency_converter.db").apply {
                CurrencyConverterDatabase.Schema.create(this)
            }
        }
         */

        single<JdbcSqliteDriver> {
            JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply {
                CurrencyConverterDatabase.Schema.create(this)
            }
        }

        single {
            CurrencyConverterDatabase(
                driver = get<JdbcSqliteDriver>(),
            )
        }

        // Provide Json instance
        single {
            Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            }
        }

        single<ThemeDataSource> {
            ThemeDataSourceImpl()
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
            HttpClient {
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
