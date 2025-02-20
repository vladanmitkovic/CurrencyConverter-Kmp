package me.mitkovic.kmp.currencyconverter.data.repository.favorites

import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    fun getFavoriteCurrencies(): Flow<List<String>>

    suspend fun updateFavoriteCurrencies(favorites: List<String>)
}
