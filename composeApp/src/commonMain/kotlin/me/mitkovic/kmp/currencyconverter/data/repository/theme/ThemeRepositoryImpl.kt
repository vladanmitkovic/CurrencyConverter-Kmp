package me.mitkovic.kmp.currencyconverter.data.repository.theme

import kotlinx.coroutines.flow.Flow
import me.mitkovic.kmp.currencyconverter.data.local.LocalDataSource

class ThemeRepositoryImpl(
    private val localDataSource: LocalDataSource,
) : ThemeRepository {

    override suspend fun saveTheme(isDarkMode: Boolean) {
        localDataSource.theme.saveTheme(isDarkMode)
    }

    override fun getTheme(): Flow<Boolean> = localDataSource.theme.getTheme()
}
