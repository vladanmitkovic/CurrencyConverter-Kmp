package me.mitkovic.kmp.currencyconverter.data.model

import org.jetbrains.compose.resources.DrawableResource

// Used for currencies symbols, names and flags
data class CurrencyDetail(
    val code: String,
    val fullName: String,
    val flagResId: DrawableResource,
)
