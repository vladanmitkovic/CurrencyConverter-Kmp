package me.mitkovic.kmp.currencyconverter.ui.screens.converter

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import me.mitkovic.kmp.currencyconverter.common.Constants.SOMETHING_WENT_WRONG
import me.mitkovic.kmp.currencyconverter.data.model.NetworkResult
import me.mitkovic.kmp.currencyconverter.data.repository.ConversionRatesRepository
import me.mitkovic.kmp.currencyconverter.logging.AppLogger

data class ConversionRatesUiState(
    val isLoading: Boolean = false,
    val rates: Rates = Rates(emptyMap()),
    val timestamp: Long? = null,
    val error: String? = null,
)

@Immutable
data class Rates(
    val rates: Map<String, Double>,
)

class ConverterViewModel(
    private val repository: ConversionRatesRepository,
    private val logger: AppLogger,
) : ViewModel() {

    val conversionRatesUiState: StateFlow<ConversionRatesUiState> =
        repository
            .getConversionRates()
            .onStart {
                emit(NetworkResult.Loading)
            }.catch { e ->
                logger.logError("Error fetching rates", e)
                emit(NetworkResult.Error(e.message ?: SOMETHING_WENT_WRONG))
            }.map { resource ->
                when (resource) {
                    is NetworkResult.Success ->
                        ConversionRatesUiState(
                            isLoading = false,
                            rates = Rates(resource.data?.conversion_rates ?: emptyMap()),
                            timestamp = resource.data?.timestamp,
                            error = null,
                        )
                    is NetworkResult.Error ->
                        ConversionRatesUiState(
                            isLoading = false,
                            rates = Rates(emptyMap()),
                            error = resource.message,
                        )
                    is NetworkResult.Loading -> ConversionRatesUiState(isLoading = true)
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = ConversionRatesUiState(),
            )

    fun logMessage(message: String) {
        logger.logError("message: $message", null)
    }

    init {
        logger.logError("ConverterViewModel", null)
        // loadQuotes()
    }

    fun loadQuotes() {
        repository
            .getConversionRates()
            .onEach { result ->
                when (result) {
                    is NetworkResult.Success -> logger.logError("AAA: ${result.data}", null)
                    is NetworkResult.Error -> {
                        logger.logError("Error: ${result.message}", result.throwable)
                    }
                    NetworkResult.Loading -> logger.logError("LOADING", null)
                }
            }.catch { e ->
                logger.logError(e.message, e)
            }.launchIn(viewModelScope)
    }
}
