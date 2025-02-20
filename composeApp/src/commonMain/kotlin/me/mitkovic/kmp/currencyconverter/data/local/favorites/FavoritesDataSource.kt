package me.mitkovic.kmp.currencyconverter.data.local.favorites

import kotlinx.coroutines.flow.Flow

interface FavoritesDataSource {

    fun getFavoriteCurrencies(): Flow<List<String>>

    suspend fun updateFavoriteCurrencies(favorites: List<String>)
}
