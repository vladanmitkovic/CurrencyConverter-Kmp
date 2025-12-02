package me.mitkovic.kmp.currencyconverter.di

import app.cash.sqldelight.db.SqlDriver
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import me.mitkovic.kmp.currencyconverter.data.local.ILocalDataSource
import me.mitkovic.kmp.currencyconverter.data.local.LocalDataSourceImpl
import me.mitkovic.kmp.currencyconverter.data.local.conversionrates.ConversionRatesDataSourceImpl
import me.mitkovic.kmp.currencyconverter.data.local.conversionrates.IConversionRatesDataSource
import me.mitkovic.kmp.currencyconverter.data.local.database.CurrencyConverterDatabase
import me.mitkovic.kmp.currencyconverter.data.local.favorites.IFavoritesDataSource
import me.mitkovic.kmp.currencyconverter.data.local.selectedcurrencies.ISelectedCurrenciesDataSource
import me.mitkovic.kmp.currencyconverter.data.local.theme.IThemeDataSource
import me.mitkovic.kmp.currencyconverter.data.remote.IRemoteDataSource
import me.mitkovic.kmp.currencyconverter.data.remote.RemoteDataSourceImpl
import me.mitkovic.kmp.currencyconverter.logging.IAppLogger
import org.koin.core.annotation.Module
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Module
class DatabaseModule {

    @Single
    fun provideDatabase(
        @Provided driver: SqlDriver,
    ): CurrencyConverterDatabase =
        CurrencyConverterDatabase(
            driver = driver,
        )

    @Single
    fun provideConversionRatesDataSource(
        database: CurrencyConverterDatabase,
        @Provided json: Json,
    ): IConversionRatesDataSource =
        ConversionRatesDataSourceImpl(
            database = database,
            json = json,
        )

    @Single
    fun provideLocalDataSource(
        conversionRates: IConversionRatesDataSource,
        @Provided theme: IThemeDataSource,
        @Provided favorites: IFavoritesDataSource,
        @Provided selectedCurrencies: ISelectedCurrenciesDataSource,
    ): ILocalDataSource =
        LocalDataSourceImpl(
            conversionRates = conversionRates,
            theme = theme,
            favorites = favorites,
            selectedCurrencies = selectedCurrencies,
        )

    @Single
    fun provideRemoteDataSource(
        @Provided client: HttpClient,
        @Provided logger: IAppLogger,
    ): IRemoteDataSource =
        RemoteDataSourceImpl(
            client = client,
            logger = logger,
        )
}
