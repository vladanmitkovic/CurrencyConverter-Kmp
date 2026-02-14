package me.mitkovic.kmp.currencyconverter.data.local.database

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
actual class DatabaseFactory {
    actual fun create(): RoomDatabase.Builder<CurrencyConverterDatabase> {
        val dbPath = documentDirectory() + "/currency_converter.db"
        return Room.databaseBuilder<CurrencyConverterDatabase>(
            name = dbPath,
        )
    }


    private fun documentDirectory(): String {
        val url = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        return requireNotNull(url?.path)
    }
}
