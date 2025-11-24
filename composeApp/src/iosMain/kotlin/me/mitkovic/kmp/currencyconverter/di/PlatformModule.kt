package me.mitkovic.kmp.currencyconverter.di

import app.cash.sqldelight.driver.native.NativeSqliteDriver
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import kotlinx.serialization.json.Json
import me.mitkovic.kmp.currencyconverter.common.IConnectivityObserver
import me.mitkovic.kmp.currencyconverter.common.IConnectivityObserverImpl
import me.mitkovic.kmp.currencyconverter.data.local.ILocalDataSource
import me.mitkovic.kmp.currencyconverter.data.local.LocalDataSourceImpl
import me.mitkovic.kmp.currencyconverter.data.local.conversionrates.ConversionRatesDataSourceImpl
import me.mitkovic.kmp.currencyconverter.data.local.conversionrates.IConversionRatesDataSource
import me.mitkovic.kmp.currencyconverter.data.local.database.CurrencyConverterDatabase
import me.mitkovic.kmp.currencyconverter.data.local.favorites.FavoritesDataSourceImpl
import me.mitkovic.kmp.currencyconverter.data.local.favorites.IFavoritesDataSource
import me.mitkovic.kmp.currencyconverter.data.local.selectedcurrencies.ISelectedCurrenciesDataSource
import me.mitkovic.kmp.currencyconverter.data.local.selectedcurrencies.SelectedCurrenciesDataSourceImpl
import me.mitkovic.kmp.currencyconverter.data.local.theme.IThemeDataSource
import me.mitkovic.kmp.currencyconverter.data.local.theme.ThemeDataSourceImpl
import me.mitkovic.kmp.currencyconverter.data.remote.IRemoteDataSource
import me.mitkovic.kmp.currencyconverter.data.remote.RemoteDataSourceImpl
import me.mitkovic.kmp.currencyconverter.data.remote.createHttpClient
import me.mitkovic.kmp.currencyconverter.logging.IAppLogger
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

actual fun platformModule() =
    module {
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

        single<IConversionRatesDataSource> {
            ConversionRatesDataSourceImpl(
                database = get<CurrencyConverterDatabase>(),
                json = get<Json>(),
            )
        }

        single<ILocalDataSource> {
            LocalDataSourceImpl(
                conversionRates = get<IConversionRatesDataSource>(),
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
            createHttpClient(Darwin, get<Json>())
        }

        single<IRemoteDataSource> {
            RemoteDataSourceImpl(
                client = get<HttpClient>(),
                logger = get<IAppLogger>(),
            )
        }
    }
