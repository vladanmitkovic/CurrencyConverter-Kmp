package me.mitkovic.kmp.currencyconverter.data.local.selectedcurrencies

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import me.mitkovic.kmp.currencyconverter.common.Constants
import java.util.prefs.Preferences

class SelectedCurrenciesDataSourceImpl : ISelectedCurrenciesDataSource {

    private val prefs = Preferences.userNodeForPackage(SelectedCurrenciesDataSourceImpl::class.java)

    companion object {
        private const val SELECTED_CURRENCY_LEFT_KEY = "selected_currency_left"
        private const val SELECTED_CURRENCY_RIGHT_KEY = "selected_currency_right"
    }

    private val selectedCurrencyLeftFlow =
        MutableStateFlow(
            prefs.get(SELECTED_CURRENCY_LEFT_KEY, null)
                ?: Constants.PREFERRED_FAVORITES.getOrElse(0) { "USD" },
        )

    private val selectedCurrencyRightFlow =
        MutableStateFlow(
            prefs.get(SELECTED_CURRENCY_RIGHT_KEY, null)
                ?: Constants.PREFERRED_FAVORITES.getOrElse(1) { "EUR" },
        )

    override suspend fun setSelectedCurrencyLeft(currencyCode: String) {
        prefs.put(SELECTED_CURRENCY_LEFT_KEY, currencyCode)
        prefs.flush()
        selectedCurrencyLeftFlow.value = currencyCode
    }

    override fun getSelectedCurrencyLeft(): Flow<String> = selectedCurrencyLeftFlow

    override suspend fun setSelectedCurrencyRight(currencyCode: String) {
        prefs.put(SELECTED_CURRENCY_RIGHT_KEY, currencyCode)
        prefs.flush()
        selectedCurrencyRightFlow.value = currencyCode
    }

    override fun getSelectedCurrencyRight(): Flow<String> = selectedCurrencyRightFlow
}
