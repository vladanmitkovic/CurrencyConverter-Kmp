package me.mitkovic.kmp.currencyconverter.data.local.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor

@Database(
    entities = [ConversionRatesEntity::class],
    version = 1,
)
@ConstructedBy(CurrencyConverterDatabaseConstructor::class)
abstract class CurrencyConverterDatabase : RoomDatabase() {
    abstract fun conversionRatesDao(): ConversionRatesDao
}

@Suppress("KotlinNoActualForExpect")
expect object CurrencyConverterDatabaseConstructor :
    RoomDatabaseConstructor<CurrencyConverterDatabase> {
    override fun initialize(): CurrencyConverterDatabase
}
