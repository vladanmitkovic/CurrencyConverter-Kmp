package me.mitkovic.kmp.currencyconverter.data.model.network

import kotlinx.serialization.Serializable

@Serializable
data class ConversionRatesResponseDto(
    val timestamp: Long,
    val conversion_rates: Map<String, Double>,
)
