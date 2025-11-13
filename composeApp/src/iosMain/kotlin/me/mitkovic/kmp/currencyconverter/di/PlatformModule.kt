package me.mitkovic.kmp.currencyconverter.di

import app.cash.sqldelight.driver.native.NativeSqliteDriver
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import me.mitkovic.kmp.currencyconverter.common.IConnectivityObserver
import me.mitkovic.kmp.currencyconverter.common.IConnectivityObserverImpl
import me.mitkovic.kmp.currencyconverter.data.local.ILocalDataSource
import me.mitkovic.kmp.currencyconverter.data.local.LocalDataSourceImpl
import me.mitkovic.kmp.currencyconverter.data.local.database.CurrencyConverterDatabase
import me.mitkovic.kmp.currencyconverter.data.local.favorites.FavoritesDataSourceImpl
import me.mitkovic.kmp.currencyconverter.data.local.favorites.IFavoritesDataSource
import me.mitkovic.kmp.currencyconverter.data.local.selectedcurrencies.ISelectedCurrenciesDataSource
import me.mitkovic.kmp.currencyconverter.data.local.selectedcurrencies.SelectedCurrenciesDataSourceImpl
import me.mitkovic.kmp.currencyconverter.data.local.theme.IThemeDataSource
import me.mitkovic.kmp.currencyconverter.data.local.theme.ThemeDataSourceImpl
import me.mitkovic.kmp.currencyconverter.data.remote.IRemoteDataSource
import me.mitkovic.kmp.currencyconverter.data.remote.RemoteDataSourceImpl
import me.mitkovic.kmp.currencyconverter.logging.AppLoggerImpl
import me.mitkovic.kmp.currencyconverter.logging.IAppLogger
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

actual fun platformModule() =
    module {
        single<IAppLogger> {
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

        single<IThemeDataSource> {
            ThemeDataSourceImpl(
                defaults = get<NSUserDefaults>(),
            )
        }

        single<IFavoritesDataSource> {
            FavoritesDataSourceImpl(
                defaults = get<NSUserDefaults>(),
            )
        }

        single<ISelectedCurrenciesDataSource> {
            SelectedCurrenciesDataSourceImpl(
                defaults = get<NSUserDefaults>(),
            )
        }

        single<ILocalDataSource> {
            LocalDataSourceImpl(
                database = get<CurrencyConverterDatabase>(),
                json = get<Json>(),
                theme = get<IThemeDataSource>(),
                favorites = get<IFavoritesDataSource>(),
                selectedCurrencies = get<ISelectedCurrenciesDataSource>(),
            )
        }

        single<IConnectivityObserver> {
            IConnectivityObserverImpl()
        }

        // Ktor HTTP client with Darwin + JSON
        single<HttpClient> {
            HttpClient(Darwin) {
                install(ContentNegotiation) {
                    json(get())
                }
            }
        }

        single<IRemoteDataSource> {
            RemoteDataSourceImpl(
                client = get<HttpClient>(),
                logger = get<IAppLogger>(),
            )
        }
    }
