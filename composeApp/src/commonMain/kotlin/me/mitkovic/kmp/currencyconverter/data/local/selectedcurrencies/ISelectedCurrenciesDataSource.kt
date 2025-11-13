package me.mitkovic.kmp.currencyconverter.data.local.selectedcurrencies

import kotlinx.coroutines.flow.Flow

interface ISelectedCurrenciesDataSource {

    suspend fun setSelectedCurrencyLeft(currencyCode: String)

    suspend fun setSelectedCurrencyRight(currencyCode: String)

    fun getSelectedCurrencyLeft(): Flow<String>

    fun getSelectedCurrencyRight(): Flow<String>
}
