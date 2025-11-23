package me.mitkovic.kmp.currencyconverter.ui.screens.converter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.mitkovic.kmp.currencyconverter.common.Constants
import me.mitkovic.kmp.currencyconverter.common.Constants.SOMETHING_WENT_WRONG
import me.mitkovic.kmp.currencyconverter.data.repository.ICurrencyConverterRepository
import me.mitkovic.kmp.currencyconverter.domain.model.Resource
import me.mitkovic.kmp.currencyconverter.logging.IAppLogger
import me.mitkovic.kmp.currencyconverter.ui.utils.CurrencyConversionUtil

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
    private val currencyConverterRepository: ICurrencyConverterRepository,
    private val logger: IAppLogger,
) : ViewModel() {

    // Companion object for constants
    private companion object {
        const val MAX_INPUT_LENGTH = 15
        const val INPUT_RESET_DELAY_MS = 5000L
    }

    private val _refreshRatesUiState = MutableStateFlow<ConversionRatesUiState>(ConversionRatesUiState.Success(emptyMap(), null))
    val refreshRatesUiState: StateFlow<ConversionRatesUiState> = _refreshRatesUiState.asStateFlow()

    // State for amount input
    private val _amountText = MutableStateFlow("1")
    val amountText: StateFlow<String> = _amountText.asStateFlow()

    // State for tracking first-time click behavior
    private val _firstTimeClicked = MutableStateFlow(false)

    // Job to manage delay cancellation
    private var resetJob: Job? = null

    val conversionRatesUiState: StateFlow<ConversionRatesUiState> =
        currencyConverterRepository
            .conversionRatesRepository
            .getConversionRates()
            .onStart {
                emit(Resource.Loading())
            }.catch { e ->
                logger.logError(ConverterViewModel::class.simpleName, "Error fetching rates", e)
                emit(Resource.Error(message = e.message ?: SOMETHING_WENT_WRONG, exception = e))
            }.map { resource ->
                when (resource) {
                    is Resource.Success ->
                        ConversionRatesUiState.Success(
                            rates = resource.data?.conversion_rates ?: emptyMap(),
                            timestamp = resource.data?.timestamp,
                        )
                    is Resource.Error ->
                        ConversionRatesUiState.Error(
                            error = resource.message ?: "Unknown error",
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
                            error = resource.message ?: "Unknown error",
                        )
                    is Resource.Loading ->
                        ConversionRatesUiState.Loading
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

    // Ordered currency list with favorites first
    val orderedCurrencies: StateFlow<List<String>> =
        favorites
            .map { favoritesList ->
                val nonFavorites = Constants.PREFERRED_CURRENCY_ORDER.filterNot { it in favoritesList }
                favoritesList + nonFavorites
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList(),
            )

    val selectedCurrencyLeft: StateFlow<String> =
        currencyConverterRepository
            .selectedCurrenciesRepository
            .getSelectedCurrencyLeft()
            .catch { e ->
                logger.logError(ConverterViewModel::class.simpleName, "Error loading selectedCurrencyLeft", e)
                emit("")
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
                emit("")
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = "",
            )

    // Extract rates map for reuse
    val rates: StateFlow<Map<String, Double>> =
        conversionRatesUiState
            .map { state ->
                when (state) {
                    is ConversionRatesUiState.Success -> state.rates
                    else -> emptyMap()
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyMap(),
            )

    // Computed amount (parsed from amountText)
    val amount: StateFlow<Double> =
        amountText
            .map { text ->
                text.toDoubleOrNull() ?: 1.0
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = 1.0,
            )

    // Computed conversion rate using combine to react to all dependencies
    val conversionRate: StateFlow<Double> =
        combine(
            selectedCurrencyLeft,
            selectedCurrencyRight,
            rates,
        ) { left, right, ratesMap ->
            CurrencyConversionUtil.getConversionRate(
                from = left,
                to = right,
                baseCurrency = Constants.BASE_CURRENCY,
                rates = ratesMap,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0,
        )

    // Computed converted amount using combine to react to both amount and rate changes
    val convertedAmount: StateFlow<Double> =
        combine(amount, conversionRate) { amt, rate ->
            amt * rate
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0,
        )

    // Handle number key clicks with proper first-time behavior, 5s reset, and length limit
    fun onNumberClick(number: String) {
        // Cancel any pending reset
        resetJob?.cancel()

        if (!_firstTimeClicked.value) {
            // First click after reset: replace the current value
            _amountText.value = number
            _firstTimeClicked.value = true
        } else {
            // Subsequent clicks: append with length limit to prevent overflow
            if (_amountText.value.length < MAX_INPUT_LENGTH) {
                _amountText.value += number
            }
        }

        // Schedule reset after 5 seconds of inactivity
        scheduleReset()
    }

    // Handle decimal button click with validation
    fun onDecimalClick() {
        // Cancel any pending reset
        resetJob?.cancel()

        // Only add decimal if not already present
        if (!_amountText.value.contains(".")) {
            _amountText.value += "."
        }

        // Mark as clicked to prevent replacement
        if (!_firstTimeClicked.value) {
            _firstTimeClicked.value = true
        }

        // Schedule reset
        scheduleReset()
    }

    // Handle delete button click
    fun onDeleteClick() {
        // Cancel any pending reset
        resetJob?.cancel()

        if (_amountText.value.isNotEmpty()) {
            _amountText.value = _amountText.value.dropLast(1)
        }

        // Don't reset to "1" on empty - let it stay empty like original
        // User can still type, and it will parse to 1.0 for calculations

        if (!_firstTimeClicked.value && _amountText.value.isNotEmpty()) {
            _firstTimeClicked.value = true
        }

        // Schedule reset only if not empty
        if (_amountText.value.isNotEmpty()) {
            scheduleReset()
        } else {
            _firstTimeClicked.value = false
        }
    }

    // Helper to schedule the reset timer
    private fun scheduleReset() {
        resetJob =
            viewModelScope.launch {
                delay(INPUT_RESET_DELAY_MS)
                _firstTimeClicked.value = false
            }
    }

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
