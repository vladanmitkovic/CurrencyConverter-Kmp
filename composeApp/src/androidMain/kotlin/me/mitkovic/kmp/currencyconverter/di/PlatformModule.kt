package me.mitkovic.kmp.currencyconverter.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
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
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual fun platformModule() =
    module {
        single<DataStore<Preferences>> {
            PreferenceDataStoreFactory.create(
                produceFile = { get<Application>().preferencesDataStoreFile("user_preferences") },
            )
        }

        single<SqlDriver> {
            AndroidSqliteDriver(
                CurrencyConverterDatabase.Schema,
                context = androidContext(),
                name = "currency_converter.db",
            )
        }

        single<CurrencyConverterDatabase> {
            CurrencyConverterDatabase(
                driver = get<SqlDriver>(),
            )
        }

        single<IThemeDataSource> {
            ThemeDataSourceImpl(
                dataStore = get<DataStore<Preferences>>(),
            )
        }

        single<IFavoritesDataSource> {
            FavoritesDataSourceImpl(
                dataStore = get<DataStore<Preferences>>(),
            )
        }

        single<ISelectedCurrenciesDataSource> {
            SelectedCurrenciesDataSourceImpl(
                dataStore = get<DataStore<Preferences>>(),
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
            IConnectivityObserverImpl(
                context = androidContext(),
            )
        }

        single<HttpClient> {
            createHttpClient(OkHttp, get<Json>())
        }

        single<IRemoteDataSource> {
            RemoteDataSourceImpl(
                client = get<HttpClient>(),
                logger = get<IAppLogger>(),
            )
        }
    }
