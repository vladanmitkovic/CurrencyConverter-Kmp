package me.mitkovic.kmp.currencyconverter.logging

interface AppLogger {

    fun logDebug(
        message: String,
        tag: String? = null,
    )

    fun logError(
        message: String?,
        throwable: Throwable?,
        tag: String? = null,
    )
}
