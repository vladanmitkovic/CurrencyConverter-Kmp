package me.mitkovic.kmp.currencyconverter.ui.utils

import me.mitkovic.kmp.currencyconverter.ui.screens.converter.Rates

object CurrencyConversionUtil {

    fun getConversionRate(
        from: String,
        to: String,
        baseCurrency: String,
        ratesWrapper: Rates,
    ): Double {
        val rates = ratesWrapper.rates
        if (from == to) return 1.0
        val rateFromToBase = rates[from] ?: return 0.0 // from -> baseCurrency
        val rateBaseToTo = rates[to] ?: return 0.0 // baseCurrency -> to
        if (from == baseCurrency) return rateBaseToTo
        if (to == baseCurrency) return 1 / rateFromToBase
        return (1 / rateFromToBase) * rateBaseToTo
    }
}
