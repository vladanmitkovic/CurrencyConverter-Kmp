package me.mitkovic.kmp.currencyconverter.data.model

data class ConversionRatesResponse(
    val result: String,
    val timestamp: Long,
    val base_currency: String,
    val conversion_rates: Map<String, Double>,
)
