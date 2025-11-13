package me.mitkovic.kmp.currencyconverter.data.local.selectedcurrencies

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import me.mitkovic.kmp.currencyconverter.common.Constants

class SelectedCurrenciesDataSourceImpl : ISelectedCurrenciesDataSource {

    private val selectedCurrencyLeftFlow =
        MutableStateFlow(
            Constants.PREFERRED_FAVORITES.getOrElse(0) { "USD" },
        )
    private val selectedCurrencyRightFlow =
        MutableStateFlow(
            Constants.PREFERRED_FAVORITES.getOrElse(1) { "EUR" },
        )

    override suspend fun setSelectedCurrencyLeft(currencyCode: String) {
        selectedCurrencyLeftFlow.value = currencyCode
    }

    override fun getSelectedCurrencyLeft(): Flow<String> = selectedCurrencyLeftFlow

    override suspend fun setSelectedCurrencyRight(currencyCode: String) {
        selectedCurrencyRightFlow.value = currencyCode
    }

    override fun getSelectedCurrencyRight(): Flow<String> = selectedCurrencyRightFlow
}
