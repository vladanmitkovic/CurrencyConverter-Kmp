package me.mitkovic.kmp.currencyconverter.data.local.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

actual class DatabaseFactory(
    private val context: Context,
) {
    actual fun create(): RoomDatabase.Builder<CurrencyConverterDatabase> {
        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath("currency_converter.db")
        return Room.databaseBuilder<CurrencyConverterDatabase>(
            context = appContext,
            name = dbFile.absolutePath,
        )
    }
}
