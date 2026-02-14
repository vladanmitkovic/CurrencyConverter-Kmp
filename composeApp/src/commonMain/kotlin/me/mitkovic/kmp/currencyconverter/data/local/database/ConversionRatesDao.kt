package me.mitkovic.kmp.currencyconverter.data.local.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversionRatesDao {

    @Upsert
    suspend fun upsert(entity: ConversionRatesEntity)

    @Query("SELECT * FROM ConversionRatesEntity WHERE id = 1")
    fun observe(): Flow<ConversionRatesEntity?>

    @Query("SELECT * FROM ConversionRatesEntity WHERE id = 1")
    suspend fun getOnce(): ConversionRatesEntity?
}
