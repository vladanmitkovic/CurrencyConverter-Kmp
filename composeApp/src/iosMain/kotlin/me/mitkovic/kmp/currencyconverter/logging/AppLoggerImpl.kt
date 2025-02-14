package me.mitkovic.kmp.currencyconverter.logging

import platform.Foundation.NSLog

class AppLoggerImpl : AppLogger {

    override fun logDebug(
        message: String,
        tag: String?,
    ) {
        NSLog("${tag ?: "Debug"}: $message")
    }

    override fun logError(
        message: String?,
        throwable: Throwable?,
        tag: String?,
    ) {
        NSLog("${tag ?: "Error"}: $message")
        throwable?.printStackTrace()
    }
}
