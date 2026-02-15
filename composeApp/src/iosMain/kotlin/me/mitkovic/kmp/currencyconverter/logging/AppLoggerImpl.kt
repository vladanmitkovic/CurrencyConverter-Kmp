package me.mitkovic.kmp.currencyconverter.logging

import platform.Foundation.NSLog

actual class AppLoggerImpl : IAppLogger {

    actual override fun logDebug(
        tag: String?,
        message: String,
    ) {
        NSLog("${tag ?: "Debug"}: $message")
    }

    actual override fun logError(
        tag: String?,
        message: String?,
        throwable: Throwable?,
    ) {
        NSLog("${tag ?: "Error"}: $message")
        throwable?.printStackTrace()
    }
}
