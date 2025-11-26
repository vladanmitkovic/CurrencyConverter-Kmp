package me.mitkovic.kmp.currencyconverter.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.mitkovic.kmp.currencyconverter.common.Constants
import me.mitkovic.kmp.currencyconverter.data.repository.ICurrencyConverterRepository
import me.mitkovic.kmp.currencyconverter.domain.model.Resource
import me.mitkovic.kmp.currencyconverter.logging.IAppLogger

class AppViewModel(
    private val currencyConverterRepository: ICurrencyConverterRepository,
    private val logger: IAppLogger,
) : ViewModel() {

    init {
        logger.logDebug(AppViewModel::class.simpleName, "AppViewModel")
        fetchConversionRates()
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

    fun updateTheme(isLightMode: Boolean) {
        viewModelScope.launch {
            currencyConverterRepository
                .themeRepository
                .saveTheme(isLightMode)
        }
    }

    fun toggleTheme() {
        theme.value?.let { isLightMode ->
            updateTheme(!isLightMode)
        }
    }

    fun fetchConversionRates() {
        viewModelScope.launch {
            val result =
                currencyConverterRepository
                    .conversionRatesRepository
                    .refreshConversionRates()
            when (result) {
                is Resource.Success -> {
                    logger.logDebug(AppViewModel::class.simpleName, "Success")
                }
                is Resource.Error -> {
                    val errorMessage = "${Constants.ERROR_FETCHING_CONVERSION_RATES}: ${result.message}"
                    result.message?.let {
                        logger.logError(AppViewModel::class.simpleName, errorMessage, Exception(it))
                    }
                }
                is Resource.Loading -> {
                    logger.logDebug(AppViewModel::class.simpleName, "Loading")
                }
            }
        }
    }
}
