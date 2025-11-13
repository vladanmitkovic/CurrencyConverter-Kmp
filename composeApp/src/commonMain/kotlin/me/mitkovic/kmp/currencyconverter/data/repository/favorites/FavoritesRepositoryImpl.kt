package me.mitkovic.kmp.currencyconverter.data.repository.favorites

import kotlinx.coroutines.flow.Flow
import me.mitkovic.kmp.currencyconverter.data.local.ILocalDataSource

class FavoritesRepositoryImpl(
    private val localDataSource: ILocalDataSource,
) : IFavoritesRepository {

    override fun getFavoriteCurrencies(): Flow<List<String>> = localDataSource.favorites.getFavoriteCurrencies()

    override suspend fun updateFavoriteCurrencies(favorites: List<String>) {
        localDataSource.favorites.updateFavoriteCurrencies(favorites)
    }
}
