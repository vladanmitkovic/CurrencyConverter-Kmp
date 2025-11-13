package me.mitkovic.kmp.currencyconverter.data.repository.selectedcurrencies

import kotlinx.coroutines.flow.Flow
import me.mitkovic.kmp.currencyconverter.data.local.ILocalDataSource

class SelectedCurrenciesRepositoryImpl(
    private val localDataSource: ILocalDataSource,
) : ISelectedCurrenciesRepository {

    override suspend fun setSelectedCurrencyLeft(currencyCode: String) {
        localDataSource.selectedCurrencies.setSelectedCurrencyLeft(currencyCode = currencyCode)
    }

    override suspend fun setSelectedCurrencyRight(currencyCode: String) {
        localDataSource.selectedCurrencies.setSelectedCurrencyRight(currencyCode = currencyCode)
    }

    override fun getSelectedCurrencyLeft(): Flow<String> = localDataSource.selectedCurrencies.getSelectedCurrencyLeft()

    override fun getSelectedCurrencyRight(): Flow<String> = localDataSource.selectedCurrencies.getSelectedCurrencyRight()
}
