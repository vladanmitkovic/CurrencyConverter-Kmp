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
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.mitkovic.kmp.currencyconverter.common.Constants
import me.mitkovic.kmp.currencyconverter.common.Constants.SOMETHING_WENT_WRONG
import me.mitkovic.kmp.currencyconverter.data.model.Resource
import me.mitkovic.kmp.currencyconverter.data.repository.CurrencyConverterRepository
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
    private val currencyConverterRepository: CurrencyConverterRepository,
    private val logger: AppLogger,
) : ViewModel() {

    private val refreshTrigger = MutableStateFlow(false)

    val conversionRatesUiState: StateFlow<ConversionRatesUiState> =
        currencyConverterRepository
            .conversionRatesRepository
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
                    is Resource.Loading ->
                        ConversionRatesUiState(
                            isLoading = true,
                            error = null,
                        )
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = ConversionRatesUiState(),
            )

    val refreshRatesUiState: StateFlow<ConversionRatesUiState> =
        refreshTrigger
            .filter { trigger -> trigger }
            .flatMapLatest {
                currencyConverterRepository
                    .conversionRatesRepository
                    .refreshConversionRates()
                    .map { resource ->
                        when (resource) {
                            is Resource.Loading ->
                                ConversionRatesUiState(
                                    isLoading = true,
                                    error = null,
                                )
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
                                    error = resource.message,
                                )
                        }
                    }.onStart {
                        emit(
                            ConversionRatesUiState(
                                isLoading = true,
                                error = null,
                            ),
                        )
                    }.catch { e ->
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

    fun refreshConversionRates(refresh: Boolean) {
        logger.logDebug(ConverterViewModel::class.simpleName, "refreshConversionRates invoked")
        refreshTrigger.value = refresh // Increment the trigger to invoke the flow
    }

    val favorites: StateFlow<List<String>> =
        currencyConverterRepository
            .favoritesRepository
            .getFavoriteCurrencies()
            .catch { e ->
                logger.logError(ConverterViewModel::class.simpleName, "Error loading favorites", e)
                emit(Constants.PREFERRED_FAVORITES)
            }.onEach { favoritesList ->
                logger.logDebug(ConverterViewModel::class.simpleName, "favoritesList: $favoritesList")
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = Constants.PREFERRED_FAVORITES,
            )

    val selectedCurrencyLeft: StateFlow<String> =
        currencyConverterRepository
            .selectedCurrenciesRepository
            .getSelectedCurrencyLeft()
            .catch { e ->
                logger.logError(ConverterViewModel::class.simpleName, "Error loading selectedCurrencyLeft", e)
                emit("") // Fallback
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = "",
            )

    val selectedCurrencyRight: StateFlow<String> =
        currencyConverterRepository
            .selectedCurrenciesRepository
            .getSelectedCurrencyRight()
            .catch { e ->
                logger.logError(ConverterViewModel::class.simpleName, "Error loading selectedCurrencyRight", e)
                emit("") // Fallback
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = "",
            )

    fun setSelectedCurrencyLeft(currency: String) {
        viewModelScope.launch {
            try {
                currencyConverterRepository
                    .selectedCurrenciesRepository
                    .setSelectedCurrencyLeft(currency)
                logger.logDebug(ConverterViewModel::class.simpleName, "Selected Currency Left set to: $currency")
            } catch (e: Exception) {
                logger.logError(ConverterViewModel::class.simpleName, "Failed to set selectedCurrencyLeft", e)
            }
        }
    }

    fun setSelectedCurrencyRight(currency: String) {
        viewModelScope.launch {
            try {
                currencyConverterRepository
                    .selectedCurrenciesRepository
                    .setSelectedCurrencyRight(currency)
                logger.logDebug(ConverterViewModel::class.simpleName, "Selected Currency Right set to: $currency")
            } catch (e: Exception) {
                logger.logError(ConverterViewModel::class.simpleName, "Failed to set selectedCurrencyRight", e)
            }
        }
    }

    fun swapCurrencies() {
        viewModelScope.launch {
            val temp = selectedCurrencyLeft.value
            setSelectedCurrencyLeft(selectedCurrencyRight.value)
            setSelectedCurrencyRight(temp)
        }
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
                    is Resource.Success -> {
                        _conversionRatesUiState.value =
                            ConversionRatesUiState(
                                isLoading = false,
                                rates = Rates(result.data?.conversion_rates ?: emptyMap()),
                                timestamp = result.data?.timestamp,
                                error = null,
                            )
                        logger.logError(ConverterViewModel::class.simpleName, "result.data: ${result.data}", null)
                    }
                    is Resource.Error -> {
                        logger.logError(ConverterViewModel::class.simpleName, "Error: ${result.message}", result.throwable)
                        _conversionRatesUiState.value =
                            ConversionRatesUiState(
                                isLoading = false,
                                rates = Rates(emptyMap()),
                                error = result.message,
                            )
                    }
                    Resource.Loading -> {
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
