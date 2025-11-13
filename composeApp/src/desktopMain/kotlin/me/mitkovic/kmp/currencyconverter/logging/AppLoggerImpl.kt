package me.mitkovic.kmp.currencyconverter.logging

class AppLoggerImpl : IAppLogger {

    override fun logDebug(
        tag: String?,
        message: String,
    ) {
        println("${tag ?: "Debug"}: $message")
    }

    override fun logError(
        tag: String?,
        message: String?,
        throwable: Throwable?,
    ) {
        println("${tag ?: "Error"}: $message")
        throwable?.printStackTrace()
    }
}
