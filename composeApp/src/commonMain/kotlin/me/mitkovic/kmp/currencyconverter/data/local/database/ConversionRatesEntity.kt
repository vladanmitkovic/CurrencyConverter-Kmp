package me.mitkovic.kmp.currencyconverter.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ConversionRatesEntity(
    @PrimaryKey val id: Long = 1L,
    val timestamp: Long,
    val rates: String,
)
