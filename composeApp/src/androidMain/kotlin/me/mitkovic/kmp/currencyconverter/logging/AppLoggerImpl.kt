package me.mitkovic.kmp.currencyconverter.logging

import timber.log.Timber

actual class AppLoggerImpl : IAppLogger {

    override fun logDebug(
        tag: String?,
        message: String,
    ) {
        Timber.tag(tag ?: "").d(message)
    }

    override fun logError(
        tag: String?,
        message: String?,
        throwable: Throwable?,
    ) {
        Timber.tag(tag ?: "").e(throwable, message)
    }
}
