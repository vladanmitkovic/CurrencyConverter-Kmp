package me.mitkovic.kmp.currencyconverter

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import me.mitkovic.kmp.currencyconverter.di.initKoin
import me.mitkovic.kmp.currencyconverter.ui.App
import org.koin.core.component.KoinComponent

fun main() {
    initKoin()
    Main().start()
}

class Main : KoinComponent {
    fun start() {
        application {
            // Create a WindowState with a fixed width and height
            val windowState = rememberWindowState(width = 500.dp, height = 800.dp)
            Window(
                onCloseRequest = ::exitApplication,
                title = "CurrencyConverter-KMP",
                state = windowState,
            ) {
                App()
            }
        }
    }
}
