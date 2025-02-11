package me.mitkovic.kmp.currencyconverter.logging

import timber.log.Timber

class AndroidLogger : AppLogger {
    override fun logDebug(
        message: String,
        tag: String?,
    ) {
        Timber.tag(tag ?: "").d(message)
    }

    override fun logError(
        message: String?,
        throwable: Throwable?,
        tag: String?,
    ) {
        Timber.tag(tag ?: "").e(throwable, message)
    }
}
