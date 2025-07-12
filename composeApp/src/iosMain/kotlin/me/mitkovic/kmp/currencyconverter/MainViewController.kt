package me.mitkovic.kmp.currencyconverter

import androidx.compose.ui.window.ComposeUIViewController
import me.mitkovic.kmp.currencyconverter.di.initKoin
import me.mitkovic.kmp.currencyconverter.ui.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    initKoin()
    return ComposeUIViewController {
        App()
    }
}
