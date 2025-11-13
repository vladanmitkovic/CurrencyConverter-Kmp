package me.mitkovic.kmp.currencyconverter.data.local.selectedcurrencies

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import me.mitkovic.kmp.currencyconverter.common.Constants
import platform.Foundation.NSUserDefaults

class SelectedCurrenciesDataSourceImpl(
    private val defaults: NSUserDefaults,
) : ISelectedCurrenciesDataSource {

    companion object {
        private const val SELECTED_CURRENCY_LEFT_KEY = "selected_currency_left"
        private const val SELECTED_CURRENCY_RIGHT_KEY = "selected_currency_right"
    }

    private val selectedCurrencyLeftFlow =
        MutableStateFlow(
            defaults.stringForKey(SELECTED_CURRENCY_LEFT_KEY)
                ?: Constants.PREFERRED_FAVORITES.getOrElse(0) { "USD" },
        )
    private val selectedCurrencyRightFlow =
        MutableStateFlow(
            defaults.stringForKey(SELECTED_CURRENCY_RIGHT_KEY)
                ?: Constants.PREFERRED_FAVORITES.getOrElse(1) { "EUR" },
        )

    override suspend fun setSelectedCurrencyLeft(currencyCode: String) {
        defaults.setObject(currencyCode, forKey = SELECTED_CURRENCY_LEFT_KEY)
        selectedCurrencyLeftFlow.value = currencyCode
    }

    override fun getSelectedCurrencyLeft(): Flow<String> = selectedCurrencyLeftFlow

    override suspend fun setSelectedCurrencyRight(currencyCode: String) {
        defaults.setObject(currencyCode, forKey = SELECTED_CURRENCY_RIGHT_KEY)
        selectedCurrencyRightFlow.value = currencyCode
    }

    override fun getSelectedCurrencyRight(): Flow<String> = selectedCurrencyRightFlow
}
