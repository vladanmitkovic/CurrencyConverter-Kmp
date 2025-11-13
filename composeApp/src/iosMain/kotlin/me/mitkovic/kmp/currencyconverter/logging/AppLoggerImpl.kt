package me.mitkovic.kmp.currencyconverter.logging

import platform.Foundation.NSLog

class AppLoggerImpl : IAppLogger {

    override fun logDebug(
        tag: String?,
        message: String,
    ) {
        NSLog("${tag ?: "Debug"}: $message")
    }

    override fun logError(
        tag: String?,
        message: String?,
        throwable: Throwable?,
    ) {
        NSLog("${tag ?: "Error"}: $message")
        throwable?.printStackTrace()
    }
}
