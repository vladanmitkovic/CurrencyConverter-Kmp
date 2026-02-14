package me.mitkovic.kmp.currencyconverter.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
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
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

actual fun platformModule() =
    module {
        single<NSUserDefaults> {
            NSUserDefaults.standardUserDefaults()
        }

        single<DatabaseFactory> {
            DatabaseFactory()
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

        single<IConnectivityObserver> {
            IConnectivityObserverImpl()
        }

        single<HttpClient> {
            createHttpClient(
                engine = Darwin,
                json = get<Json>(),
            )
        }
    }
