package me.mitkovic.kmp.currencyconverter.data.repository.theme

import kotlinx.coroutines.flow.Flow
import me.mitkovic.kmp.currencyconverter.data.local.ILocalDataSource

class ThemeRepositoryImpl(
    private val localDataSource: ILocalDataSource,
) : IThemeRepository {

    override suspend fun saveTheme(isDarkMode: Boolean) {
        localDataSource.theme.saveTheme(isDarkMode)
    }

    override fun getTheme(): Flow<Boolean> = localDataSource.theme.getTheme()
}
