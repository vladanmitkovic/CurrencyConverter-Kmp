package me.mitkovic.kmp.currencyconverter.ui.screens.converter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
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

sealed class ConversionRatesUiState {
    object Loading : ConversionRatesUiState()

    data class Success(
        val rates: Map<String, Double>,
        val timestamp: Long?,
    ) : ConversionRatesUiState()

    data class Error(
        val error: String,
    ) : ConversionRatesUiState()
}

class ConverterViewModel(
    private val currencyConverterRepository: CurrencyConverterRepository,
    private val logger: AppLogger,
) : ViewModel() {

    private val _refreshRatesUiState = MutableStateFlow<ConversionRatesUiState>(ConversionRatesUiState.Success(emptyMap(), null))
    val refreshRatesUiState: StateFlow<ConversionRatesUiState> = _refreshRatesUiState.asStateFlow()

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
                        ConversionRatesUiState.Success(
                            rates = resource.data?.conversion_rates ?: emptyMap(),
                            timestamp = resource.data?.timestamp,
                        )
                    is Resource.Error ->
                        ConversionRatesUiState.Error(
                            error = resource.message,
                        )
                    is Resource.Loading ->
                        ConversionRatesUiState.Loading
                }
            }.onEach { uiState ->
                logger.logDebug(ConverterViewModel::class.simpleName, "UI state updated: $uiState")
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = ConversionRatesUiState.Success(emptyMap(), null),
            )

    fun refreshConversionRates() {
        logger.logDebug(ConverterViewModel::class.simpleName, "refreshConversionRates invoked")
        viewModelScope.launch {
            _refreshRatesUiState.value = ConversionRatesUiState.Loading
            try {
                val resource =
                    currencyConverterRepository
                        .conversionRatesRepository
                        .refreshConversionRates()
                _refreshRatesUiState.value =
                    when (resource) {
                        is Resource.Success ->
                            ConversionRatesUiState.Success(
                                rates = resource.data?.conversion_rates ?: emptyMap(),
                                timestamp = resource.data?.timestamp,
                            )
                        is Resource.Error ->
                            ConversionRatesUiState.Error(
                                error = resource.message,
                            )
                        is Resource.Loading ->
                            ConversionRatesUiState.Loading
                    }
            } catch (e: Exception) {
                _refreshRatesUiState.value =
                    ConversionRatesUiState.Error(
                        error = e.message ?: "Unknown error",
                    )
            }
        }
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

    fun logMessage(message: String) = logger.logError(ConverterViewModel::class.simpleName, message, null)

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
