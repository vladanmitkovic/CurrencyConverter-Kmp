package me.mitkovic.kmp.currencyconverter

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "CurrencyConverter-KMP",
    ) {
        App()
    }
}