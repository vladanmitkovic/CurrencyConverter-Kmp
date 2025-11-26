package me.mitkovic.kmp.currencyconverter.data.repository.theme

import kotlinx.coroutines.flow.Flow
import me.mitkovic.kmp.currencyconverter.data.local.ILocalDataSource

class ThemeRepositoryImpl(
    private val localDataSource: ILocalDataSource,
) : IThemeRepository {

    override suspend fun saveTheme(isLightMode: Boolean) {
        localDataSource.theme.saveTheme(isLightMode = isLightMode)
    }

    override fun getTheme(): Flow<Boolean> = localDataSource.theme.getTheme()
}
