package me.mitkovic.kmp.currencyconverter.data.model

import me.mitkovic.kmp.currencyconverter.domain.model.ConversionRatesResponse as DomainConversionRatesResponse

fun ConversionRatesResponse.toDomainModel(): DomainConversionRatesResponse =
    DomainConversionRatesResponse(
        timestamp = this.timestamp,
        conversion_rates = this.conversion_rates,
    )

fun DomainConversionRatesResponse.toDataModel(): ConversionRatesResponse =
    ConversionRatesResponse(
        timestamp = this.timestamp,
        conversion_rates = this.conversion_rates,
    )
