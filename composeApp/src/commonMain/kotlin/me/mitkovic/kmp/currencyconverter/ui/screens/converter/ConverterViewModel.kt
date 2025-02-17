package me.mitkovic.kmp.currencyconverter.ui.screens.converter

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
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

@OptIn(ExperimentalCoroutinesApi::class)
class ConverterViewModel(
    private val repository: ConversionRatesRepository,
    private val logger: AppLogger,
) : ViewModel() {

    private val refreshTrigger = MutableStateFlow(0)

    val conversionRatesUiState: StateFlow<ConversionRatesUiState> =
        repository
            .getConversionRates()
            .onStart {
                emit(NetworkResult.Loading)
            }.catch { e ->
                logger.logError(ConverterViewModel::class.simpleName, "Error fetching rates", e)
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
        logger.logError(ConverterViewModel::class.simpleName, "message: $message", null)
    }

    val refreshRatesUiState: StateFlow<ConversionRatesUiState> =
        refreshTrigger
            .filter { trigger -> trigger > 0 }
            .flatMapLatest {
                repository
                    .refreshConversionRates()
                    .map { resource ->
                        when (resource) {
                            is NetworkResult.Loading -> ConversionRatesUiState(isLoading = true)
                            is NetworkResult.Success ->
                                ConversionRatesUiState(
                                    isLoading = false,
                                    rates = Rates(resource.data?.conversion_rates ?: emptyMap()),
                                    timestamp = resource.data?.timestamp,
                                )
                            is NetworkResult.Error ->
                                ConversionRatesUiState(
                                    isLoading = false,
                                    error = resource.message,
                                )
                        }
                    }.onStart { emit(ConversionRatesUiState(isLoading = true)) }
                    .catch { e ->
                        emit(
                            ConversionRatesUiState(
                                isLoading = false,
                                error = e.message ?: "Unknown error",
                            ),
                        )
                    }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = ConversionRatesUiState(),
            )

    fun refreshConversionRates(refresh: Int) {
        logger.logDebug(ConverterViewModel::class.simpleName, "refreshConversionRates invoked")
        refreshTrigger.value = refresh // Increment the trigger to invoke the flow
    }
}
