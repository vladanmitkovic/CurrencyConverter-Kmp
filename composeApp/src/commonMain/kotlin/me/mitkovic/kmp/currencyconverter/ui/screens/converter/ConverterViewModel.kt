package me.mitkovic.kmp.currencyconverter.ui.screens.converter

import androidx.lifecycle.ViewModel
import me.mitkovic.kmp.currencyconverter.logging.AppLogger

class ConverterViewModel(
    private val logger: AppLogger,
) : ViewModel() {

    init {
        logger.logError("ConverterViewModel", null)
        loadQuotes()
    }

    fun loadQuotes() {
    }
}
