package me.mitkovic.kmp.currencyconverter.domain.model

data class ConversionRatesResponse(
    val timestamp: Long,
    val conversion_rates: Map<String, Double>,
)
