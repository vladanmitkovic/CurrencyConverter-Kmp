package me.mitkovic.kmp.currencyconverter.di

import kotlinx.serialization.json.Json
import me.mitkovic.kmp.currencyconverter.data.local.ILocalDataSource
import me.mitkovic.kmp.currencyconverter.data.remote.IRemoteDataSource
import me.mitkovic.kmp.currencyconverter.data.repository.CurrencyConverterRepositoryImpl
import me.mitkovic.kmp.currencyconverter.data.repository.ICurrencyConverterRepository
import me.mitkovic.kmp.currencyconverter.data.repository.conversionrates.ConversionRatesRepositoryImpl
import me.mitkovic.kmp.currencyconverter.data.repository.favorites.FavoritesRepositoryImpl
import me.mitkovic.kmp.currencyconverter.data.repository.favorites.IFavoritesRepository
import me.mitkovic.kmp.currencyconverter.data.repository.selectedcurrencies.ISelectedCurrenciesRepository
import me.mitkovic.kmp.currencyconverter.data.repository.selectedcurrencies.SelectedCurrenciesRepositoryImpl
import me.mitkovic.kmp.currencyconverter.data.repository.theme.IThemeRepository
import me.mitkovic.kmp.currencyconverter.data.repository.theme.ThemeRepositoryImpl
import me.mitkovic.kmp.currencyconverter.domain.repository.IConversionRatesRepository
import me.mitkovic.kmp.currencyconverter.logging.AppLoggerImpl
import me.mitkovic.kmp.currencyconverter.logging.IAppLogger
import me.mitkovic.kmp.currencyconverter.platform.Platform
import me.mitkovic.kmp.currencyconverter.platform.getPlatform
import org.koin.core.annotation.Module
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Module
class AppModule {

    @Single
    fun providePlatform(): Platform = getPlatform()

    @Single
    fun provideAppLogger(): IAppLogger = AppLoggerImpl()

    @Single
    fun provideJson(): Json =
        Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            isLenient = true
        }

    @Single
    fun provideConversionRatesRepository(
        @Provided localDataSource: ILocalDataSource,
        @Provided remoteDataSource: IRemoteDataSource,
        logger: IAppLogger,
    ): IConversionRatesRepository =
        ConversionRatesRepositoryImpl(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
            logger = logger,
        )

    @Single
    fun provideThemeRepository(
        @Provided localDataSource: ILocalDataSource,
    ): IThemeRepository =
        ThemeRepositoryImpl(
            localDataSource = localDataSource,
        )

    @Single
    fun provideFavoritesRepository(
        @Provided localDataSource: ILocalDataSource,
    ): IFavoritesRepository =
        FavoritesRepositoryImpl(
            localDataSource = localDataSource,
        )

    @Single
    fun provideSelectedCurrenciesRepository(
        @Provided localDataSource: ILocalDataSource,
    ): ISelectedCurrenciesRepository =
        SelectedCurrenciesRepositoryImpl(
            localDataSource = localDataSource,
        )

    @Single
    fun provideCurrencyConverterRepository(
        conversionRatesRepository: IConversionRatesRepository,
        themeRepository: IThemeRepository,
        favoritesRepository: IFavoritesRepository,
        selectedCurrenciesRepository: ISelectedCurrenciesRepository,
    ): ICurrencyConverterRepository =
        CurrencyConverterRepositoryImpl(
            conversionRatesRepository = conversionRatesRepository,
            themeRepository = themeRepository,
            favoritesRepository = favoritesRepository,
            selectedCurrenciesRepository = selectedCurrenciesRepository,
        )
}
