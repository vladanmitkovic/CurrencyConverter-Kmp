package me.mitkovic.kmp.currencyconverter.data.repository.selectedcurrencies

import kotlinx.coroutines.flow.Flow

interface ISelectedCurrenciesRepository {

    suspend fun setSelectedCurrencyLeft(currencyCode: String)

    suspend fun setSelectedCurrencyRight(currencyCode: String)

    fun getSelectedCurrencyLeft(): Flow<String>

    fun getSelectedCurrencyRight(): Flow<String>
}
