package me.mitkovic.kmp.currencyconverter.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
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

actual fun platformModule() =
    module {
        single<DatabaseFactory> {
            DatabaseFactory()
        }

        single<IThemeDataSource> {
            ThemeDataSourceImpl()
        }

        single<IFavoritesDataSource> {
            FavoritesDataSourceImpl()
        }

        single<ISelectedCurrenciesDataSource> {
            SelectedCurrenciesDataSourceImpl()
        }

        single<IConnectivityObserver> {
            IConnectivityObserverImpl()
        }

        single<HttpClient> {
            createHttpClient(
                engine = CIO,
                json = get<Json>(),
            )
        }
    }
