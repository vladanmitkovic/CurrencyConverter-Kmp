package me.mitkovic.kmp.currencyconverter

import android.app.Application
import me.mitkovic.kmp.currencyconverter.di.initKoin
import org.koin.android.ext.koin.androidContext
import timber.log.Timber

class CurrencyConverterApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        initKoin {
            androidContext(this@CurrencyConverterApp)
        }
    }
}
