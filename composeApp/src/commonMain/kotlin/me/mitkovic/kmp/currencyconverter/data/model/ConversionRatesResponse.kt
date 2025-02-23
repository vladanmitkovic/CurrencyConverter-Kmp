package me.mitkovic.kmp.currencyconverter.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ConversionRatesResponse(
    val timestamp: Long,
    val conversion_rates: Map<String, Double>,
)
