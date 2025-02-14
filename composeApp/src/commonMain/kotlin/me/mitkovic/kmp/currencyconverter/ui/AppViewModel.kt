package me.mitkovic.kmp.currencyconverter.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.mitkovic.kmp.currencyconverter.data.local.LocalDataSource
import me.mitkovic.kmp.currencyconverter.logging.AppLogger

class AppViewModel(
    private val localDataSource: LocalDataSource,
    logger: AppLogger,
) : ViewModel() {

    init {
        logger.logError("AppViewModel", null)
    }

    val theme =
        localDataSource
            .getTheme()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null, // No default theme until loaded
            )

    fun updateTheme(isDarkMode: Boolean) {
        viewModelScope.launch {
            localDataSource
                .saveTheme(isDarkMode)
        }
    }
}
