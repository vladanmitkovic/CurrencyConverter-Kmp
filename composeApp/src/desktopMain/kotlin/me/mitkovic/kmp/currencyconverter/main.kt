package me.mitkovic.kmp.currencyconverter

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import me.mitkovic.kmp.currencyconverter.di.initKoin
import org.koin.core.component.KoinComponent

fun main() {
    initKoin()
    Main().start()
}

class Main : KoinComponent {
    fun start() {
        application {
            Window(
                onCloseRequest = ::exitApplication,
                title = "CurrencyConverter-KMP",
            ) {
                App()
            }
        }
    }
}
