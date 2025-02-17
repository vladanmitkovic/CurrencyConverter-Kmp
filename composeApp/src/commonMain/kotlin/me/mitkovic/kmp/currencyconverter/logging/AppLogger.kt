package me.mitkovic.kmp.currencyconverter.logging

interface AppLogger {

    fun logDebug(
        tag: String? = null,
        message: String,
    )

    fun logError(
        tag: String? = null,
        message: String?,
        throwable: Throwable?,
    )
}
