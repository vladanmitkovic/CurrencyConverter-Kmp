package me.mitkovic.kmp.currencyconverter.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import me.mitkovic.kmp.currencyconverter.common.ConnectivityObserver
import me.mitkovic.kmp.currencyconverter.common.NetworkConnectivityObserver
import me.mitkovic.kmp.currencyconverter.data.local.LocalDataSource
import me.mitkovic.kmp.currencyconverter.data.local.LocalDataSourceImpl
import me.mitkovic.kmp.currencyconverter.logging.AndroidLogger
import me.mitkovic.kmp.currencyconverter.logging.AppLogger
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual fun platformModule() =
    module {
        single<AppLogger> { AndroidLogger() }

        single<DataStore<Preferences>> {
            PreferenceDataStoreFactory.create(
                produceFile = { get<Application>().preferencesDataStoreFile("user_preferences") },
            )
        }

        single<LocalDataSource> {
            LocalDataSourceImpl(
                dataStore = get<DataStore<Preferences>>(),
            )
        }

        single<ConnectivityObserver> {
            NetworkConnectivityObserver(
                context = androidContext(),
            )
        }
    }
