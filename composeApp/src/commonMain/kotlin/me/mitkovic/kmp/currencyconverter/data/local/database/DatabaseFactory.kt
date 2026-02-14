package me.mitkovic.kmp.currencyconverter.data.local.database

import androidx.room.RoomDatabase

expect class DatabaseFactory {
    fun create(): RoomDatabase.Builder<CurrencyConverterDatabase>
}
