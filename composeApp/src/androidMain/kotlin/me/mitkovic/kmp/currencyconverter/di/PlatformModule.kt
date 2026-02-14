package me.mitkovic.kmp.currencyconverter.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.serialization.json.Json
import me.mitkovic.kmp.currencyconverter.common.IConnectivityObserver
import me.mitkovic.kmp.currencyconverter.common.IConnectivityObserverImpl
import me.mitkovic.kmp.currencyconverter.data.local.database.DatabaseFactory
import me.mitkovic.kmp.currencyconverter.data.local.favorites.FavoritesDataSourceImpl
import me.mitkovic.kmp.currencyconverter.data.local.favorites.IFavoritesDataSource
import me.mitkovic.kmp.currencyconverter.data.local.selectedcurrencies.ISelectedCurrenciesDataSource
import me.mitkovic.kmp.currencyconverter.data.local.selectedcurrencies.SelectedCurrenciesDataSourceImpl
import me.mitkovic.kmp.currencyconverter.data.local.theme.IThemeDataSource
import me.mitkovic.kmp.currencyconverter.data.local.theme.ThemeDataSourceImpl
import me.mitkovic.kmp.currencyconverter.data.remote.createHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual fun platformModule() =
    module {
        single<DataStore<Preferences>> {
            PreferenceDataStoreFactory.create(
                produceFile = { androidContext().preferencesDataStoreFile("user_preferences") },
            )
        }

        single<DatabaseFactory> {
            DatabaseFactory(
                context = androidContext(),
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

        single<IConnectivityObserver> {
            IConnectivityObserverImpl(
                context = androidContext(),
            )
        }

        single<HttpClient> {
            createHttpClient(
                engine = OkHttp,
                json = get<Json>(),
            )
        }
    }
