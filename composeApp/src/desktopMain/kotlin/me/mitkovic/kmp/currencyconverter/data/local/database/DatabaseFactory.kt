package me.mitkovic.kmp.currencyconverter.data.local.database

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

actual class DatabaseFactory {
    actual fun create(): RoomDatabase.Builder<CurrencyConverterDatabase> {
        val dbFile = File(System.getProperty("java.io.tmpdir"), "currency_converter.db")
        return Room.databaseBuilder<CurrencyConverterDatabase>(
            name = dbFile.absolutePath,
        )
    }
}
