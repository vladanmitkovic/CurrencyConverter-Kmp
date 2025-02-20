package me.mitkovic.kmp.currencyconverter.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.mitkovic.kmp.currencyconverter.data.repository.CurrencyConverterRepository
import me.mitkovic.kmp.currencyconverter.logging.AppLogger

class AppViewModel(
    private val currencyConverterRepository: CurrencyConverterRepository,
    logger: AppLogger,
) : ViewModel() {

    init {
        logger.logError(AppViewModel::class.simpleName, "AppViewModel", null)
    }

    val theme =
        currencyConverterRepository
            .themeRepository
            .getTheme()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null, // No default theme until loaded
            )

    fun updateTheme(isDarkMode: Boolean) {
        viewModelScope.launch {
            currencyConverterRepository
                .themeRepository
                .saveTheme(isDarkMode)
        }
    }
}
