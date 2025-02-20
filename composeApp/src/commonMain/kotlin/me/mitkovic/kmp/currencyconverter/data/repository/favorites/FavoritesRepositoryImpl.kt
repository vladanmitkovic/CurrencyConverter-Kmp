package me.mitkovic.kmp.currencyconverter.data.repository.favorites

import kotlinx.coroutines.flow.Flow
import me.mitkovic.kmp.currencyconverter.data.local.LocalDataSource

class FavoritesRepositoryImpl(
    private val localDataSource: LocalDataSource,
) : FavoritesRepository {

    override fun getFavoriteCurrencies(): Flow<List<String>> = localDataSource.favorites.getFavoriteCurrencies()

    override suspend fun updateFavoriteCurrencies(favorites: List<String>) {
        localDataSource.favorites.updateFavoriteCurrencies(favorites)
    }
}
