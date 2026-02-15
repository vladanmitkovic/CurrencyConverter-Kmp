package me.mitkovic.kmp.currencyconverter.logging

expect class AppLoggerImpl() : IAppLogger {
    override fun logDebug(
        tag: String?,
        message: String,
    )

    override fun logError(
        tag: String?,
        message: String?,
        throwable: Throwable?,
    )
}
