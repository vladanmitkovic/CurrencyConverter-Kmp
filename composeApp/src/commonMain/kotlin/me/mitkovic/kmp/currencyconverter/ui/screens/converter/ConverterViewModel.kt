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
import me.mitkovic.kmp.currencyconverter.data.model.Resource
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
                emit(Resource.Loading)
            }.catch { e ->
                logger.logError(ConverterViewModel::class.simpleName, "Error fetching rates", e)
                emit(Resource.Error(e.message ?: SOMETHING_WENT_WRONG))
            }.map { resource ->
                when (resource) {
                    is Resource.Success ->
                        ConversionRatesUiState(
                            isLoading = false,
                            rates = Rates(resource.data?.conversion_rates ?: emptyMap()),
                            timestamp = resource.data?.timestamp,
                            error = null,
                        )
                    is Resource.Error ->
                        ConversionRatesUiState(
                            isLoading = false,
                            rates = Rates(emptyMap()),
                            error = resource.message,
                        )
                    is Resource.Loading -> ConversionRatesUiState(isLoading = true)
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
                            is Resource.Loading -> ConversionRatesUiState(isLoading = true)
                            is Resource.Success ->
                                ConversionRatesUiState(
                                    isLoading = false,
                                    rates = Rates(resource.data?.conversion_rates ?: emptyMap()),
                                    timestamp = resource.data?.timestamp,
                                )
                            is Resource.Error ->
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

    /*
    private val _conversionRatesUiState = MutableStateFlow(ConversionRatesUiState())
    val conversionRatesUiState: StateFlow<ConversionRatesUiState> = _conversionRatesUiState.asStateFlow()

    init {
        logger.logError(ConverterViewModel::class.simpleName, "ConverterViewModel", null)
        loadQuotes()
    }

    fun loadQuotes() {
        repository
            .getConversionRates()
            .onEach { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        _conversionRatesUiState.value =
                            ConversionRatesUiState(
                                isLoading = false,
                                rates = Rates(result.data?.conversion_rates ?: emptyMap()),
                                timestamp = result.data?.timestamp,
                                error = null,
                            )
                        logger.logError(ConverterViewModel::class.simpleName, "result.data: ${result.data}", null)
                    }
                    is NetworkResult.Error -> {
                        logger.logError(ConverterViewModel::class.simpleName, "Error: ${result.message}", result.throwable)
                        _conversionRatesUiState.value =
                            ConversionRatesUiState(
                                isLoading = false,
                                rates = Rates(emptyMap()),
                                error = result.message,
                            )
                    }
                    NetworkResult.Loading -> {
                        logger.logError(ConverterViewModel::class.simpleName, "LOADING", null)
                        _conversionRatesUiState.value = ConversionRatesUiState(isLoading = true)
                    }
                }
            }.catch { e ->
                logger.logError(ConverterViewModel::class.simpleName, e.message, e)
            }.launchIn(viewModelScope)
    }
     */
}
