package me.mitkovic.kmp.currencyconverter.data.mapper

import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import me.mitkovic.kmp.currencyconverter.data.local.database.ConversionRatesEntity
import me.mitkovic.kmp.currencyconverter.data.model.network.ConversionRatesResponseDto
import me.mitkovic.kmp.currencyconverter.domain.model.ConversionRatesResponse

// ============================================
// DTO (Network Response) -> Domain
// ============================================

fun ConversionRatesResponseDto.toDomain(): ConversionRatesResponse =
    ConversionRatesResponse(
        timestamp = this.timestamp,
        conversion_rates = this.conversion_rates,
    )

// ============================================
// Domain -> DTO
// ============================================

fun ConversionRatesResponse.toDto(): ConversionRatesResponseDto =
    ConversionRatesResponseDto(
        timestamp = this.timestamp,
        conversion_rates = this.conversion_rates,
    )

// ============================================
// Entity (SQLDelight) -> Domain
// ============================================

fun ConversionRatesEntity.toDomain(json: Json): ConversionRatesResponse =
    ConversionRatesResponse(
        timestamp = this.timestamp,
        conversion_rates =
            json.decodeFromString(
                MapSerializer(String.serializer(), Double.serializer()),
                this.rates,
            ),
    )

// ============================================
// Domain -> Entity JSON String (for SQLDelight insertion)
// ============================================

fun ConversionRatesResponse.toEntityJsonString(json: Json): String =
    json.encodeToString(
        MapSerializer(String.serializer(), Double.serializer()),
        this.conversion_rates,
    )
