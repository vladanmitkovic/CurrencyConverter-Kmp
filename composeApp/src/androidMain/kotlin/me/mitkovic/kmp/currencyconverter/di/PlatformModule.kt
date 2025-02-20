package me.mitkovic.kmp.currencyconverter.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import kotlinx.serialization.json.Json
import me.mitkovic.kmp.currencyconverter.common.ConnectivityObserver
import me.mitkovic.kmp.currencyconverter.common.ConnectivityObserverImpl
import me.mitkovic.kmp.currencyconverter.common.Constants
import me.mitkovic.kmp.currencyconverter.data.local.LocalDataSource
import me.mitkovic.kmp.currencyconverter.data.local.LocalDataSourceImpl
import me.mitkovic.kmp.currencyconverter.data.local.database.CurrencyConverterDatabase
import me.mitkovic.kmp.currencyconverter.data.local.favorites.FavoritesDataSource
import me.mitkovic.kmp.currencyconverter.data.local.favorites.FavoritesDataSourceImpl
import me.mitkovic.kmp.currencyconverter.data.local.selectedcurrencies.SelectedCurrenciesDataSource
import me.mitkovic.kmp.currencyconverter.data.local.selectedcurrencies.SelectedCurrenciesDataSourceImpl
import me.mitkovic.kmp.currencyconverter.data.local.theme.ThemeDataSource
import me.mitkovic.kmp.currencyconverter.data.local.theme.ThemeDataSourceImpl
import me.mitkovic.kmp.currencyconverter.data.remote.ApiService
import me.mitkovic.kmp.currencyconverter.data.remote.RemoteDataSource
import me.mitkovic.kmp.currencyconverter.data.remote.RemoteDataSourceImpl
import me.mitkovic.kmp.currencyconverter.logging.AppLogger
import me.mitkovic.kmp.currencyconverter.logging.AppLoggerImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

actual fun platformModule() =
    module {
        single<AppLogger> { AppLoggerImpl() }

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

        single {
            Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            }
        }

        single<ThemeDataSource> {
            ThemeDataSourceImpl(
                dataStore = get<DataStore<Preferences>>(),
            )
        }

        single<FavoritesDataSource> {
            FavoritesDataSourceImpl(
                dataStore = get<DataStore<Preferences>>(),
            )
        }

        single<SelectedCurrenciesDataSource> {
            SelectedCurrenciesDataSourceImpl(
                dataStore = get<DataStore<Preferences>>(),
            )
        }

        single<LocalDataSource> {
            LocalDataSourceImpl(
                database = get<CurrencyConverterDatabase>(),
                json = get<Json>(),
                theme = get<ThemeDataSource>(),
                favorites = get<FavoritesDataSource>(),
                selectedCurrencies = get<SelectedCurrenciesDataSource>(),
            )
        }

        single<ConnectivityObserver> {
            ConnectivityObserverImpl(
                context = androidContext(),
            )
        }

        single {
            OkHttpClient
                .Builder()
                .addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    },
                ).build()
        }

        single {
            Retrofit
                .Builder()
                .baseUrl(Constants.BASE_URL)
                .client(get<OkHttpClient>())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        single {
            get<Retrofit>().create(ApiService::class.java)
        }

        single<RemoteDataSource> {
            RemoteDataSourceImpl(
                apiService = get<ApiService>(),
                logger = get<AppLogger>(),
            )
        }
    }
